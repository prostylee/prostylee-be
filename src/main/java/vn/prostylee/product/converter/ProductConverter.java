package vn.prostylee.product.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.response.*;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.service.*;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.StatusLikeRequest;
import vn.prostylee.useractivity.service.UserLikeService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConverter {

    private final LocationService locationService;
    private final FileUploadService fileUploadService;
    private final UserService userService;
    private final ProductStoreService productStoreService;
    private final ProductStatisticService productStatisticService;
    private final ProductImageService productImageService;
    private final UserLikeService userLikeService;
    private final ProductAttributeService productAttributeService;
    private final AttributeService attributeService;
    private final ProductPriceService productPriceService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    public ProductResponse toResponse(Product product) {
        ProductResponse productResponse = BeanUtil.copyProperties(product, ProductResponse.class);
        productResponse.setImageUrls(buildImageUrls(product.getId()));
        productResponse.setLocation(buildLocation(productResponse.getLocationId()));
        productResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
        productResponse.setProductOwnerResponse(buildProductOwner(product));
        productResponse.setProductStatisticResponse(buildProductStatistic(product.getId()));
        productResponse.setLikeStatusOfUserLogin(getLikeStatusOfUserLogin(product.getId()));
        productResponse.setProductAttributeOptionResponse(buildAttributeOption(product.getId()));
        productResponse.setProductPriceResponseList(buildProductPrice(product.getId()));
        productResponse.setCategoryResponse(buildCategory(product.getCategoryId()));
        productResponse.setBrandResponse(buildBrand(product.getBrandId()));
        return productResponse;
    }

    public ProductResponseLite toResponseForListStore(Product product) {
        ProductResponseLite productResponseLite = BeanUtil.copyProperties(product, ProductResponseLite.class);
        List<String> imageUrls = buildImageUrls(product.getId());
        productResponseLite.setImageUrl(imageUrls.isEmpty() ? null : imageUrls.get(0));
        return productResponseLite;
    }

    private List<String> buildImageUrls(long productID) {
        List<Long> attachmentIds = Optional.ofNullable(productID)
                .map(productImageService::getAttachmentIdByProductID)
                .orElse(null);
        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            try {
                return fileUploadService.getImageUrls(attachmentIds, ImageSize.PRODUCT_SIZE.getWidth(), ImageSize.PRODUCT_SIZE.getHeight());
            } catch (ResourceNotFoundException e) {
                log.debug("Could not build image Urls from attachmentIds={}", attachmentIds, e);
            }
        }
        return Collections.emptyList();
    }

    private LocationResponse buildLocation(Long locationId) {
        try {
            return Optional.ofNullable(locationId)
                    .map(locationService::findById)
                    .orElse(null);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    private ProductOwnerResponse buildProductOwner(Product product) {
        final ProductOwnerResponse[] productOwnerResponse = new ProductOwnerResponse[1];
        if (product.getStoreId() != null) {
            productOwnerResponse[0] = productStoreService.getStoreOwner(product.getStoreId());
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

    private ProductStatisticResponse buildProductStatistic(Long id){
        return Optional.ofNullable(id)
                .flatMap(productStatisticService::fetchById)
                .orElse(null);
    }

    private Boolean getLikeStatusOfUserLogin(Long productId){
        StatusLikeRequest request = StatusLikeRequest.builder()
                .targetIds(Collections.singletonList(productId))
                .targetType(TargetType.PRODUCT.name())
                .build();
        List<Long> result = userLikeService.loadStatusLikes(request);
        if (result.stream().count() > 0){
            return true;
        }
        return false;
    }

    private List<ProductAttributeOptionResponse> buildAttributeOption(Long productId){
        List<ProductAttribute> productAttributeList = productAttributeService.getProductAttributeByProductId(productId);
        Set<Long> attributeIds = new HashSet<>();
        productAttributeList.stream()
                .filter(e -> attributeIds.add(e.getAttribute().getId()))
                .collect(Collectors.toList());
        List<ProductAttributeOptionResponse> reponse = new ArrayList<>();
        for (Long attrId: attributeIds
             ) {
            AttributeResponse attributeResponse = attributeService.findById(attrId);
            ProductAttributeOptionResponse item = BeanUtil.copyProperties(attributeResponse,ProductAttributeOptionResponse.class);
            List<ProductAttributeResponse> productAttributeResponses = productAttributeList.stream()
                    .filter(productAttribute -> attrId.equals(productAttribute.getAttribute().getId()))
                    .map(e -> {
                        return BeanUtil.copyProperties(e,ProductAttributeResponse.class);
                    }).collect(Collectors.toList());
            item.setProductAttributeResponses(productAttributeResponses.stream()
                    .filter(distinctByKey(e -> e.getAttrValue()))
                    .collect(Collectors.toList()));
            reponse.add(item);
        }
        return reponse;
    }

    private List<ProductPriceResponse> buildProductPrice(Long productId) {
        List<ProductPrice> productPriceList = productPriceService.getProductPricesByProduct(productId);
        List<ProductPriceResponse> productPriceResponseList = productPriceList.stream()
                .map(e -> {
                    ProductPriceResponse obj = BeanUtil.copyProperties(e,ProductPriceResponse.class);
                    obj.setProductAttributes(productAttributeService.getProductAttributeByPriceId(e.getId())
                            .stream().map(p -> {
                                return BeanUtil.copyProperties(p,ProductAttributeResponse.class);
                            }).collect(Collectors.toList()));
                    return obj;
                }).collect(Collectors.toList());
        return productPriceResponseList;
    }

    private CategoryResponse buildCategory(Long categoryId) {
        return Optional.ofNullable(categoryId)
                .map(categoryService::findById)
                .orElse(null);
    }

    private BrandResponse buildBrand(Long brandId) {
        return Optional.ofNullable(brandId)
                .map(brandService::findById)
                .orElse(null);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> uniqueMap = new ConcurrentHashMap<>();
        return t -> uniqueMap.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
