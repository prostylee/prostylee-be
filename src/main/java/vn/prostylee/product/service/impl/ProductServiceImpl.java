package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.constant.ProductStatus;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductOwnerResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.*;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.*;

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
    @Override
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

    @Override
    public ProductResponse findById(Long id) {
        return this.toResponse(this.getProductById(id));
    }

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Product productEntity = BeanUtil.copyProperties(productRequest, Product.class);
        return toResponse(productRequest, productEntity);
    }

    private ProductResponse toResponse(ProductRequest productRequest, Product productEntity) {
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

        Product savedProductEntity = productRepository.save(productEntity);
        Long productId = savedProductEntity.getId();

        productRequest.getProductPriceRequest().forEach(productPriceRequest -> {
            productPriceRequest.setProductId(productId);
            productPriceService.save(productPriceRequest);
        });


        ProductResponse productResponse = BeanUtil.copyProperties(savedProductEntity, ProductResponse.class);
        productResponse.setBrandId(savedProductEntity.getBrand().getId());
        productResponse.setCategoryId(savedProductEntity.getCategory().getId());

        return productResponse;
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

            if (productResponse.getId() % RandomUtils.nextInt(1, 5) == 0) { // TODO get ads from ads table
                productResponse.setIsAdvertising(true);
            }

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
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found with id [" + id + "]"));
    }

    @Override
    public long countTotalProductByUser(Long userId) {
        return productRepository.countProductsByCreatedBy(userId);
    }
}