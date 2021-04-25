package vn.prostylee.product.specification;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.specs.QueryBuilder;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.repository.ProductAttributeRepository;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.PaidStoreRequest;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserFollowerService;

import javax.persistence.criteria.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductSpecificationBuilder {
    private final BaseFilterSpecs<Product> baseFilterSpecs;
    private final OrderService orderService;
    private final UserFollowerService userFollowerService;
    private final StoreService storeService;
    private final ProductAttributeRepository productAttributeRepository;

    public Specification<Product> buildSearchable(ProductFilter productFilter) {
        Specification<Product> mainSpec = (root, query, cb) -> {
            QueryBuilder queryBuilder = new QueryBuilder<>(cb, root);
            findByUser(productFilter, queryBuilder);
            findByCategory(productFilter, queryBuilder);
            findByStore(productFilter, queryBuilder);
            if (isAttributesAvailable(productFilter.getAttributes())) {
                findByAttributes(root, productFilter.getAttributes(), queryBuilder);
            }

            if (BooleanUtils.isTrue(productFilter.getBestSeller())) {
                root.join( "statistic", JoinType.LEFT);
            }

            Predicate[] orPredicates = queryBuilder.build();
            return cb.and(orPredicates);
        };

        //TODO will config in database and try another way to show useful
        Set<Long> storeIds =  new LinkedHashSet<>();
        //storeIds.addAll(getAdsStores(5, productFilter));
        storeIds.addAll(getTopFollowingStores(5, productFilter));
        storeIds.addAll(getPaidStores(5, productFilter));
        storeIds.addAll(getNewStores(30, productFilter));
        getProductSpecification(mainSpec,  new ArrayList<>(storeIds));

        if (StringUtils.isNotBlank(productFilter.getKeyword())) {
            Specification<Product> searchSpec = baseFilterSpecs.search(productFilter);
            mainSpec = mainSpec.and(searchSpec);
        }
        return mainSpec;
    }

    private Specification<Product> getProductSpecification(Specification<Product> spec, List<Long> storeIds) {
        if (CollectionUtils.isNotEmpty(storeIds)) {
            spec = spec.and((root, query, cb) -> {
                CriteriaBuilder.In<Long> inClause = cb.in(root.get("storeId"));
                storeIds.forEach(inClause::value);
                return inClause;
            });
        }
        return spec;
    }

    //TODO
    /*private List<Long> getAdsStores(int numberOfTakeProduct, ProductFilter productFilter){
        return new ArrayList<>();
    }*/

    private List<Long> getNewStores(int numberOfTakeProduct, ProductFilter productFilter) {
        NewestStoreRequest request = NewestStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfTakeProduct);
        return storeService.getNewStoreIds(request);
    }

    private List<Long> getPaidStores(int numberOfTakeProduct, ProductFilter productFilter) {
        PaidStoreRequest request = PaidStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfTakeProduct);
        return orderService.getPaidStores(request);
    }

    private List<Long> getTopFollowingStores(int numberOfTakeProduct, ProductFilter productFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.STORE.name()))
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime()).build();
        request.setLimit(numberOfTakeProduct);
        return userFollowerService.getTopBeFollows(request);
    }

  /*  private List<Long> buildNewestProducts(List<Long> storeIds) {
        List<Long> productIds = new ArrayList();
        for (Long storeId: storeIds) {
            NewestProductRequest newestProductRequest = NewestProductRequest.builder().storeId(storeId).build();
            productIds.addAll(this.getNewestProduct(newestProductRequest));
        }
        return productIds;
    }

     private List<Long> getNewestProduct(NewestProductRequest request) {
        Pageable pageSpecification = PageRequest.of(request.getPage(), request.getNumberOfProducts());
        return productRepository.findNewestProductIdByIds(request.getStoreId(), request.getFromDate()
                , request.getToDate(), pageSpecification);
    }
    */

    private void findByUser(ProductFilter productFilter, QueryBuilder queryBuilder) {
        queryBuilder.equals("createdBy", productFilter.getUserId());
    }

    private void findByCategory(ProductFilter productFilter, QueryBuilder queryBuilder) {
        queryBuilder.equalsRef("category", "id", productFilter.getCategoryId(), JoinType.INNER);
    }

    private void findByStore(ProductFilter productFilter, QueryBuilder queryBuilder) {
        queryBuilder.equals("storeId", productFilter.getStoreId());
    }

    private boolean isAttributesAvailable(Map<String, String> attributes) {
        return MapUtils.isNotEmpty(attributes);
    }
    private void findByAttributes(Root root, Map<String, String> attributesRequest, QueryBuilder queryBuilder) {
        Join<Product, ProductPrice> joinProductPrice = root.join( "productPrices");
        List<Long> attrs = findByAttributes(attributesRequest);
        queryBuilder.valueIn(joinProductPrice, "id", attrs.toArray());
    }

    private List<Long> findByAttributes(Map<String, String> attributesRequest) {
        return productAttributeRepository.findCrossTabProductAttribute(attributesRequest);
    }
}
