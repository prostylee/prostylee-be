package vn.prostylee.product.specification;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.NewestProductRequest;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.PaidStoreRequest;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserFollowerService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSpecificationBuilder {
    private final BaseFilterSpecs<Product> baseFilterSpecs;
    private final OrderService orderService;
    private final UserFollowerService userFollowerService;
    private final StoreService storeService;
    private final ProductRepository productRepository;

    public Specification<Product> buildSearchable(ProductFilter productFilter) {
        Specification<Product> spec = baseFilterSpecs.search(productFilter);

        if (productFilter.getStoreId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("storeId"), productFilter.getStoreId()));
        }

        spec = getProductByTopFollowingStores(spec, productFilter);
        spec = buildPaidStore(spec, productFilter);
        spec = buildNewProductOfNewStore(spec, productFilter);

        if (BooleanUtils.isTrue(productFilter.getBestSeller())) {
            spec = buildBestSellerSpec(spec, productFilter);
        }

        // TODO query by new feeds
//        switch (productFilter.getNewFeedType()) {
//            case USER:
//                break;
//            case STORE:
//                break;
//            default:
//                break;
//        }
        return spec;
    }

    private Specification<Product> buildNewProductOfNewStore(Specification<Product> spec, ProductFilter productFilter) {
        NewestStoreRequest request = NewestStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(productFilter.getLimit());
        request.setPage(productFilter.getPage());
        List<Long> storeIds = storeService.getNewStoreIds(request);
        List<Long> productIds = buildNewestProducts(storeIds);
        return getProductSpecification(spec, productIds);
    }

    private Specification<Product> buildPaidStore(Specification<Product> spec, ProductFilter productFilter) {
        PaidStoreRequest request = PaidStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(productFilter.getLimit());
        request.setPage(productFilter.getPage());
        List<Long> storeIds = orderService.getPaidStores(request);
        List<Long> productIds = buildNewestProducts(storeIds);
        return getProductSpecification(spec, productIds);
    }

    private Specification<Product> getProductByTopFollowingStores(Specification<Product> spec, ProductFilter productFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.STORE.name()))
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(productFilter.getLimit());
        request.setPage(productFilter.getPage());
        List<Long> storeIds = userFollowerService.getTopBeFollows(request);
        List<Long> productIds = buildNewestProducts(storeIds);
        return getProductSpecification(spec, productIds);
    }

    private List<Long> buildNewestProducts(List<Long> storeIds) {
        List<Long> productIds = new ArrayList();
        for (Long storeId: storeIds) {
            NewestProductRequest newestProductRequest = NewestProductRequest.builder().storeId(storeId).build();
            productIds.addAll(this.getNewestProduct(newestProductRequest));
        }
        return productIds;
    }

    private Specification<Product> buildBestSellerSpec(Specification<Product> spec, ProductFilter productFilter) {
        BestSellerFilter bestSellerFilter = BestSellerFilter.builder()
                .storeId(productFilter.getStoreId()).build();
        bestSellerFilter.setLimit(productFilter.getLimit());
        bestSellerFilter.setPage(productFilter.getPage());
        return getProductSpecification(spec, orderService.getBestSellerProductIds(bestSellerFilter));
    }

    private Specification<Product> getProductSpecification(Specification<Product> spec, List<Long> productIds) {
        if (CollectionUtils.isNotEmpty(productIds)) {
            spec = spec.and((root, query, cb) -> {
                CriteriaBuilder.In<Long> inClause = cb.in(root.get("id"));
                productIds.forEach(inClause::value);
                return inClause;
            });
        }
        return spec;
    }

    private List<Long> getNewestProduct(NewestProductRequest request) {
        Pageable pageSpecification = PageRequest.of(request.getPage(), request.getNumberOfProducts());
        return productRepository.findNewestProductIdByIds(request.getStoreId(), request.getFromDate()
                , request.getToDate(), pageSpecification);
    }
}
