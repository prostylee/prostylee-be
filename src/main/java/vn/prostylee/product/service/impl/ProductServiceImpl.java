package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.constant.ProductStatus;
import vn.prostylee.product.converter.ProductConverter;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.*;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.*;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.PaidStoreRequest;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserFollowerService;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final BaseFilterSpecs<Product> baseFilterSpecs;
    private final ProductRepository productRepository;
    private final LocationService locationService;
    private final ProductImageService productImageService;
    private final ProductPaymentTypeService productPaymentTypeService;
    private final ProductShippingProviderService productShippingProviderService;
    private final ProductPriceService productPriceService;
    private final OrderService orderService;
    private final ProductConverter productConverter;
    private final UserFollowerService userFollowerService;
    private final StoreService storeService;
    @Override
    public Page<ProductResponse> findAll(BaseFilter baseFilter) {
        ProductFilter productFilter = (ProductFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(productFilter);
        Page<Product> page = this.productRepository.findAllActive(buildSearchable(productFilter), pageable);
        return page.map(productConverter::toResponse);
    }

    private Specification<Product> buildSearchable(ProductFilter productFilter) {
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
        NewestStoreRequest request =  NewestStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build();
        request.setLimit(productFilter.getLimit());
        request.setPage(productFilter.getPage());
        List<Long> storeIds = storeService.getNewStoreIds(request);

        //TODO get random product
        List<Long> productIds = new ArrayList();
        return getProductSpecification(spec, productIds);
    }

    private Specification<Product> buildPaidStore(Specification<Product> spec, ProductFilter productFilter) {
        PaidStoreRequest request = PaidStoreRequest.builder()
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build();
        request.setLimit(productFilter.getLimit());
        request.setPage(productFilter.getPage());

        List<Long> storeIds = orderService.getPaidStores(request);
        //TODO get random product
        List<Long> productIds = new ArrayList();
        return getProductSpecification(spec, productIds);
    }

    private Specification<Product> getProductByTopFollowingStores(Specification<Product> spec, ProductFilter productFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.STORE.name()))
                .fromDate(DateUtils.getLastDaysBefore(productFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build();
        request.setLimit(productFilter.getLimit());
        request.setPage(productFilter.getPage());
        List<Long> storeIds = userFollowerService.getTopBeFollows(request);

        //TODO get  random product
        List<Long> productIds = new ArrayList();
        return getProductSpecification(spec, productIds);
    }

    private Specification<Product> buildBestSellerSpec(Specification<Product> spec, ProductFilter productFilter) {
            BestSellerFilter bestSellerFilter = BestSellerFilter.builder()
                    .storeId(productFilter.getStoreId())
                    .build();
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


    @Override
    public ProductResponse findById(Long id) {
        return productConverter.toResponse(this.getProductById(id));
    }

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Product product = saveProduct(productRequest);
        saveProductPrice(product.getId(), productRequest.getProductPriceRequest());

        ProductResponse productResponse = BeanUtil.copyProperties(product, ProductResponse.class);
        productResponse.setBrandId(product.getBrand().getId());
        productResponse.setCategoryId(product.getCategory().getId());
        return productResponse;
    }

    private Product saveProduct(ProductRequest productRequest) {
        Product productEntity = BeanUtil.copyProperties(productRequest, Product.class);
        Long locationId = saveLocation(productRequest.getLocationRequest());
        productEntity.setLocationId(locationId);
        productEntity.setStatus(ProductStatus.PUBLISHED.getStatus());
        productEntity.setPublishedDate(new Date());
        productEntity.setBrand(new Brand(productRequest.getBrandId()));
        productEntity.setCategory(new Category(productRequest.getCategoryId()));

        Set<ProductImage> savedImages = productImageService
                .handleProductImages(productRequest.getProductImageRequests(), productEntity);
        productEntity.setProductImages(savedImages);

        Set<ProductPaymentType> productPaymentTypes = productPaymentTypeService
                .buildProductPaymentTypes(productRequest.getPaymentTypes(), productEntity);
        productEntity.setProductPaymentTypes(productPaymentTypes);

        Set<ProductShippingProvider> productShippingProviders = productShippingProviderService
                .buildProductShippingProviders(productRequest.getShippingProviders(), productEntity);
        productEntity.setProductShippingProviders(productShippingProviders);

        return productRepository.save(productEntity);
    }

    private void saveProductPrice(Long productId, List<ProductPriceRequest> productPrices) {
        productPrices.forEach(productPriceRequest -> {
            productPriceRequest.setProductId(productId);
            productPriceService.save(productPriceRequest);
        });
    }

    private Long saveLocation(LocationRequest locationRequest) {
        return locationService.save(locationRequest).getId();
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = this.getProductById(id);
        BeanUtil.mergeProperties(productRequest, product);
        return productConverter.toResponse(this.productRepository.save(product));
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.productRepository.softDelete(id);
            log.info("Product with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Product is not found with id [" + id + "]");
        }
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found with id [" + id + "]"));
    }

    @Override
    public long countTotalProductByUser(Long userId) {
        return productRepository.countProductsByCreatedBy(userId);
    }
}