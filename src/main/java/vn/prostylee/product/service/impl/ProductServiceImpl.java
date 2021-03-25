package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.configuration.monitor.annotation.UserBehaviorTracking;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.constant.ProductStatus;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductOwnerResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.*;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final BaseFilterSpecs<Product> baseFilterSpecs;
    private final ProductRepository productRepository;
    private final LocationService locationService;
    private final ProductImageService productImageService;
    private final ProductPaymentTypeService productPaymentTypeService;
    private final ProductShippingProviderService productShippingProviderService;
    private final ProductPriceService productPriceService;
    private final FileUploadService fileUploadService;
    private final OrderService orderService;

    @Override
    @UserBehaviorTracking
    public Page<ProductResponse> findAll(BaseFilter baseFilter) {
        ProductFilter productFilter = (ProductFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(productFilter);
        Page<Product> page = this.productRepository.findAllActive(buildSearchable(productFilter), pageable);
        return page.map(this::toResponse);
    }

    private Specification<Product> buildSearchable(ProductFilter productFilter) {
        Specification<Product> spec = baseFilterSpecs.search(productFilter);

        if (productFilter.getStoreId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("storeId"), productFilter.getStoreId()));
        }

        spec = buildBestSellerSpec(spec, productFilter);

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

    private Specification<Product> buildBestSellerSpec(Specification<Product> spec, ProductFilter productFilter) {
        if (BooleanUtils.isTrue(productFilter.getBestSeller())) {
            BestSellerFilter bestSellerFilter = BestSellerFilter.builder()
                    .storeId(productFilter.getStoreId())
                    .build();
            bestSellerFilter.setLimit(productFilter.getLimit());
            bestSellerFilter.setPage(productFilter.getPage());
            List<Long> productIds = orderService.getBestSellerProductIds(bestSellerFilter);
            if (CollectionUtils.isNotEmpty(productIds)) { // Get best-seller if exists, otherwise ignore this condition
                spec = spec.and((root, query, cb) -> {
                    CriteriaBuilder.In<Long> inClause = cb.in(root.get("id"));
                    productIds.forEach(inClause::value);
                    return inClause;
                });
            }
        }
        return spec;
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse productResponse = BeanUtil.copyProperties(product, ProductResponse.class);
        Set<ProductImage> productImages = product.getProductImages();
        List<Long> attachmentIds = productImages.stream().map(ProductImage::getAttachmentId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            List<String> imageUrls = fileUploadService.getImageUrls(attachmentIds, ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
            productResponse.setImageUrls(imageUrls);
        }

        try {

            if (productResponse.getLocationId() != null) {
                productResponse.setLocation(locationService.findById(productResponse.getLocationId()));
            }

            productResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
            productResponse.setProductOwnerResponse(ProductOwnerResponse.builder()
                    .id(product.getStoreId() != null ? product.getStoreId() : product.getCreatedBy())
                    .name("Lorem Ipsum")
                    .logoUrl(product.getStoreId() != null
                            ? "https://thebucketofkai2020.s3.ap-southeast-1.amazonaws.com/0a93f4e8-fca5-492f-9853-f5b6f3b28334.png"
                            : "https://thebucketofkai2020.s3.ap-southeast-1.amazonaws.com/3b939cd9-3768-4b5a-812f-2a7face512d2.jpeg")
                    .build());
        } catch (Exception e) {
            log.debug("Can not get avatar", e);
        }

        return productResponse;
    }

    @Override
    @UserBehaviorTracking
    public ProductResponse findById(Long id) {
        return this.toResponse(this.getProductById(id));
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
        Long locationId = fetchLocation(productRequest.getLocationRequest());
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

    private Long fetchLocation(LocationRequest locationRequest) {
        LocationResponse response = locationService.save(locationRequest);
        return response.getId();
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = this.getProductById(id);
        BeanUtil.mergeProperties(productRequest, product);
        return toResponse(this.productRepository.save(product));
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.productRepository.softDelete(id);
            log.info("Product with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Product id {} does not exists", id);
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