package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.product.constant.ProductStatus;
import vn.prostylee.product.converter.ProductConverter;
import vn.prostylee.product.dto.filter.NewFeedsFilter;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.RecentViewProductFilter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.NewFeedResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.entity.*;
import vn.prostylee.product.repository.ProductExtRepository;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.*;
import vn.prostylee.useractivity.constant.TrackingType;
import vn.prostylee.useractivity.dto.filter.UserTrackingFilter;
import vn.prostylee.useractivity.service.UserTrackingService;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductExtRepository productExtRepository;
    private final LocationService locationService;
    private final ProductImageService productImageService;
    private final ProductPaymentTypeService productPaymentTypeService;
    private final ProductShippingProviderService productShippingProviderService;
    private final ProductPriceService productPriceService;
    private final ProductConverter productConverter;
    private final UserTrackingService userTrackingService;
    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public Page<ProductResponse> findAll(BaseFilter baseFilter) {
        Page<Product> page = productExtRepository.findAll((ProductFilter) baseFilter);
        List<ProductResponse> responses = page.getContent()
                .stream()
                .map(productConverter::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, page.getPageable(), page.getTotalElements());
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = this.getById(id);
        BeanUtil.mergeProperties(productRequest, product);
        return productConverter.toResponse(this.productRepository.save(product));
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            return this.productRepository.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Product is not found with id [" + id + "]");
        }
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product is not found with id [" + id + "]"));
    }

    @Override
    public List<ProductResponse> getRecentViewProducts(RecentViewProductFilter recentViewProductFilter) {
        UserTrackingFilter userTrackingFilter = UserTrackingFilter.builder()
                .userId(authenticatedProvider.getUserIdValue())
                .trackingType(TrackingType.PRODUCT)
                .build();
        userTrackingFilter.setLimit(recentViewProductFilter.getLimit());
        userTrackingFilter.setPage(recentViewProductFilter.getPage());

        List<Long> productIds = userTrackingService.getLastVisitedIdsBy(userTrackingFilter);

        return productRepository.findProductsByIds(productIds)
                .stream()
                .map(productConverter::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse findById(Long id) {
        return productConverter.toResponse(this.getById(id));
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

    @Override
    public long countTotalProductByUser(Long userId) {
        return productRepository.countProductsByCreatedBy(userId);
    }

    private Product saveProduct(ProductRequest productRequest) {
        Product productEntity = BeanUtil.copyProperties(productRequest, Product.class);
        if(productRequest.getStoreId() != null) {
            Long locationId = saveLocation(productRequest.getLocationRequest());
            productEntity.setLocationId(locationId);
        }
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
    public Page<ProductResponseLite> getProductsForListStore(BaseFilter baseFilter) {
        Page<Product> page = productExtRepository.findAll((ProductFilter) baseFilter);
        List<ProductResponseLite> responses = page.getContent()
                .stream()
                .map(productConverter::toResponseForListStore)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, page.getPageable(), page.getTotalElements());
    }

    @Override
    public List<ProductResponseLite> findByIds(List<Long> productIds) {
        return productRepository.findProductsByIds(productIds)
                .stream()
                .map(productConverter::toResponseForListStore)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NewFeedResponse> getNewFeeds(NewFeedsFilter newFeedsFilter){
        Page<NewFeedResponse> page = productExtRepository.findNewFeedsOfStore(newFeedsFilter);
        List<NewFeedResponse> newFeeds = page.stream()
                .map(item->productConverter.toResponseForNewFeed(item, newFeedsFilter.getTargetType()))
                .collect(Collectors.toList());
        return new PageImpl<>(newFeeds, page.getPageable(), page.getTotalElements());
    }

}