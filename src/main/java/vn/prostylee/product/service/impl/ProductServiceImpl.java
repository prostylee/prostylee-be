package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.specs.QueryBuilder;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.constant.ProductStatus;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.RelatedProductFilter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductOwnerResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.*;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.*;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.MostActiveUserFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserMostActiveService;

import javax.persistence.criteria.*;
import java.util.*;
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
    private final UserService userService;
    private final UserMostActiveService userMostActiveService;
    private StoreService storeService;
    private final AttributeService attributeService;

    private static Map<String, Long> attrCollection;

    @Override
    public Page<ProductResponse> findAll(BaseFilter baseFilter) {
        ProductFilter productFilter = (ProductFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(productFilter);
        Page<Product> page = this.productRepository.findAllActive(buildSearchable(productFilter), pageable);
        return page.map(this::toResponse);
    }

    private Specification<Product> buildSearchable(ProductFilter productFilter) {
        Specification<Product> mainSpec = (root, query, cb) -> {
            QueryBuilder queryBuilder = new QueryBuilder<>(cb, root);
            findByUser(productFilter, queryBuilder);
            findByCategory(productFilter, queryBuilder);
            findByStore(productFilter, queryBuilder);
            if (isAttributesAvailable(productFilter.getAttributes())) {
                findByAttributes(root, productFilter.getAttributes(), queryBuilder);
            }
            Predicate[] orPredicates = queryBuilder.build();
            return cb.and(orPredicates);
        };
//        Specification<Product> spec = buildBestSellerSpec(spec, productFilter);
        if (StringUtils.isNotBlank(productFilter.getKeyword())) {
            Specification<Product> searchSpec = baseFilterSpecs.search(productFilter);
            mainSpec = mainSpec.and(searchSpec);
        }
        return mainSpec;
    }

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
        Join<ProductPrice, ProductAttribute> joinProductAttr = joinProductPrice.join("productAttributes");
//        Join<ProductAttribute, Attribute> joinAttr = joinProductAttr.join("attribute");
        findByAttributes(attributesRequest, queryBuilder, joinProductAttr);
    }

    private void findByAttributes( Map<String, String> attributesRequest, QueryBuilder queryBuilder,
                            Join<ProductPrice, ProductAttribute> source) {
        if(MapUtils.isEmpty(attrCollection)) {
            attrCollection = attributeService.getAllAttributes();
        }
        for(Map.Entry attribute : attributesRequest.entrySet()) {
            Long attrId = attrCollection.get(attribute.getKey());
            Path<Long> idFromSource = source.get("attribute").get("id");
            queryBuilder.equalsAsId(idFromSource, attrId);
            queryBuilder.likeIgnoreCaseMultiTableRef("attrValue", attribute.getValue(), source);
        }
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
        productResponse.setImageUrls(buildImageUrls(product.getProductImages()));
        productResponse.setLocation(buildLocation(productResponse.getLocationId()));
        productResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
        productResponse.setProductOwnerResponse(buildProductOwner(product));
        return productResponse;
    }

    private List<String> buildImageUrls(Set<ProductImage> productImages) {
        List<Long> attachmentIds = productImages.stream()
                .map(ProductImage::getAttachmentId)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            try {
                return fileUploadService.getImageUrls(attachmentIds, ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
            } catch (ResourceNotFoundException e) {
                log.debug("Could not build image Urls from attachmentIds={}", attachmentIds, e);
            }
        }
        return Collections.emptyList();
    }

    private LocationResponse buildLocation(Long locationId) {
        return Optional.ofNullable(locationId)
                .flatMap(locationService::fetchById)
                .orElse(null);
    }

    private ProductOwnerResponse buildProductOwner(Product product) {
        final ProductOwnerResponse[] productOwnerResponse = new ProductOwnerResponse[1];
        if (product.getStoreId() != null) {
            storeService.fetchById(product.getStoreId()).ifPresent(store ->
                    productOwnerResponse[0] = ProductOwnerResponse.builder()
                            .id(store.getId())
                            .name(store.getName())
                            .logoUrl(store.getLogoUrl())
                            .build());
        } else {
            userService.fetchById(product.getCreatedBy()).ifPresent(user ->
                    productOwnerResponse[0] = ProductOwnerResponse.builder()
                            .id(user.getId())
                            .name(user.getFullName())
                            .logoUrl(user.getAvatar())
                            .build());
        }
        return productOwnerResponse[0];
    }

    @Override
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
        return toResponse(this.productRepository.save(product));
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
    public Page<ProductResponse> getRelatedProducts(Long productId, RelatedProductFilter relatedProductFilter) {
        Optional<Product> optProduct = productRepository.findById(productId);
        Optional<Category> optCategory = optProduct.map(Product::getCategory);
        if (optProduct.isEmpty() || optCategory.isEmpty()) {
            return Page.empty();
        }

        Category category = optCategory.get();

        if (BooleanUtils.isTrue(relatedProductFilter.getHot())) {
            List<Long> productIds = getRelatedProductIdsByMostActive(optProduct.get(), relatedProductFilter);
            if (CollectionUtils.isNotEmpty(productIds)) {
                return getRelatedProductsByMostActive(productIds);
            }
        }
        return getRelatedProductsByNewest(productId, category.getId(), relatedProductFilter.getLimit(), relatedProductFilter.getPage());
    }

    private List<Long> getRelatedProductIdsByMostActive(Product product, RelatedProductFilter relatedProductFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.PRODUCT.name()))
                .customFieldId1(product.getCategory().getId())
                .fromDate(DateUtils.getLastDaysBefore(MostActiveUserFilter.DEFAULT_TIME_RANGE_IN_DAYS))
                .toDate(Calendar.getInstance().getTime())
                .build()
                .pagingParam(new PagingParam(relatedProductFilter.getLimit() + 1, relatedProductFilter.getPage()));

        List<Long> productIds = userMostActiveService.getTargetIdsByMostActive(request);
        productIds.remove(product.getId());
        if (productIds.size() > relatedProductFilter.getLimit()) {
            productIds = productIds.subList(0, relatedProductFilter.getLimit());
        }
        return productIds;
    }

    private Page<ProductResponse> getRelatedProductsByMostActive(List<Long> productIds) {
        List<ProductResponse> products = productRepository.findProductsByIds(productIds)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(products);
    }

    private Page<ProductResponse> getRelatedProductsByNewest(Long productId, Long categoryId, int limit, int offset) {
        Sort sort = Sort.by("createdAt");
        Pageable pageable = PageRequest.of(offset, limit, sort);
        return productRepository.getRelatedProducts(productId, categoryId, pageable)
                .map(this::toResponse);
    }

    @Override
    public long countTotalProductByUser(Long userId) {
        return productRepository.countProductsByCreatedBy(userId);
    }

    @Autowired
    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }
}