package vn.prostylee.product.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.post.dto.response.PostStatisticResponse;
import vn.prostylee.post.service.PostImageService;
import vn.prostylee.post.service.PostStatisticService;
import vn.prostylee.product.dto.response.*;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.service.*;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.dto.request.StatusLikeRequest;
import vn.prostylee.useractivity.dto.response.UserWishListResponse;
import vn.prostylee.useractivity.service.UserLikeService;
import vn.prostylee.useractivity.service.UserWishListService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductConverter {

    private final LocationService locationService;
    private final FileUploadService fileUploadService;
    private final UserService userService;
    private final ProductStoreService productStoreService;
    private final ProductStatisticService productStatisticService;
    private final ProductImageService productImageService;
    private final UserLikeService userLikeService;
    private final ProductAttributeService productAttributeService;
    private final ProductPriceService productPriceService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final UserWishListService userWishListService;
    private final PostImageService postImageService;
    private final PostStatisticService postStatisticService;
    private final StoreService storeService;

    public ProductConverter(@Lazy UserWishListService userWishListService,
                            LocationService locationService,
                            FileUploadService fileUploadService,
                            UserService userService,
                            ProductStoreService productStoreService,
                            ProductStatisticService productStatisticService,
                            ProductImageService productImageService,
                            UserLikeService userLikeService,
                            ProductAttributeService productAttributeService,
                            ProductPriceService productPriceService,
                            CategoryService categoryService,
                            BrandService brandService,
                            PostImageService postImageService,
                            PostStatisticService postStatisticService,
                            StoreService storeService) {
        this.locationService = locationService;
        this.fileUploadService = fileUploadService;
        this.userService = userService;
        this.productStoreService = productStoreService;
        this.productStatisticService = productStatisticService;
        this.productImageService = productImageService;
        this.userLikeService = userLikeService;
        this.productAttributeService = productAttributeService;
        this.productPriceService = productPriceService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.userWishListService = userWishListService;
        this.postImageService = postImageService;
        this.postStatisticService = postStatisticService;
        this.storeService = storeService;
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse productResponse = BeanUtil.copyProperties(product, ProductResponse.class);
        productResponse.setImageUrls(buildImageUrls(product.getId()));
        productResponse.setLocation(buildLocation(productResponse.getLocationId()));
        productResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
        productResponse.setProductOwnerResponse(buildProductOwner(product));
        productResponse.setProductStatisticResponse(buildProductStatistic(product.getId()));
        productResponse.setLikeStatusOfUserLogin(getLikeStatusOfUserLogin(product.getId(), TargetType.PRODUCT));
        productResponse.setProductAttributeOptionResponse(buildAttributeOption(product.getId()));
        productResponse.setProductPriceResponseList(buildProductPrice(product.getId()));
        productResponse.setCategoryResponse(buildCategory(product.getCategoryId()));
        productResponse.setBrandResponse(buildBrand(product.getBrandId()));
        productResponse.setSaveStatusOfUserLogin(getSaveStatusOfUserLogin(product.getId()));
        return productResponse;
    }

    public ProductResponseLite toResponseForListStore(Product product) {
        ProductResponseLite productResponseLite = BeanUtil.copyProperties(product, ProductResponseLite.class);
        List<String> imageUrls = buildImageUrls(product.getId());
        productResponseLite.setImageUrl(imageUrls.isEmpty() ? null : imageUrls.get(0));
        productResponseLite.setCategoryId(Optional.ofNullable(product.getCategory()).map(Category::getId).orElse(null));
        return productResponseLite;
    }

    public NewFeedResponse toResponseForNewFeed(NewFeedResponse newFeedResponse, TargetType targetType) {
        if (TargetType.valueOf(newFeedResponse.getType()) == TargetType.PRODUCT) {
            newFeedResponse.setImageUrls(buildImageUrls(newFeedResponse.getId()));
            newFeedResponse.setLikeStatusOfUserLogin(getLikeStatusOfUserLogin(newFeedResponse.getId(), TargetType.PRODUCT));
            newFeedResponse.setSaveStatusOfUserLogin(getSaveStatusOfUserLogin(newFeedResponse.getId()));
            newFeedResponse.setProductStatisticResponse(buildProductStatistic(newFeedResponse.getId()));
        }
        if (TargetType.valueOf(newFeedResponse.getType()) == TargetType.POST) {
            newFeedResponse.setImageUrls(buildPostImageUrls(newFeedResponse.getId()));
            newFeedResponse.setLikeStatusOfUserLogin(getLikeStatusOfUserLogin(newFeedResponse.getId(), TargetType.POST));
            newFeedResponse.setPostStatisticResponse(buildPostStatistic(newFeedResponse.getId()));
            newFeedResponse.setStoreAdsResponseLite(getStoreResponseLite(newFeedResponse.getStoreAdsId()));
        }
        newFeedResponse.setNewFeedOwnerResponse(buildNewFeedOwner(newFeedResponse.getOwnerId(), targetType));
        newFeedResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
        return newFeedResponse;
    }

    private List<String> buildImageUrls(Long productId) {
        List<Long> attachmentIds = Optional.ofNullable(productId)
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

    private List<String> buildPostImageUrls(Long postId) {
        List<Long> attachmentIds = Optional.ofNullable(postId)
                .map(postImageService::getAttachmentIdByPostID)
                .orElse(null);
        if (CollectionUtils.isNotEmpty(attachmentIds)) {
            try {
                return fileUploadService.getImageUrls(attachmentIds, ImageSize.POST_SIZE.getWidth(), ImageSize.POST_SIZE.getHeight());
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

    private ProductStatisticResponse buildProductStatistic(Long id) {
        return Optional.ofNullable(id)
                .flatMap(productStatisticService::fetchById)
                .orElse(null);
    }

    private PostStatisticResponse buildPostStatistic(Long id) {
        return Optional.ofNullable(id)
                .flatMap(postStatisticService::fetchById)
                .orElse(null);
    }

    private Boolean getLikeStatusOfUserLogin(Long id, TargetType targetType) {
        StatusLikeRequest request = StatusLikeRequest.builder()
                .targetIds(Collections.singletonList(id))
                .targetType(targetType)
                .build();
        List<Long> result = userLikeService.loadStatusLikes(request);
        return CollectionUtils.isNotEmpty(result);
    }

    private Boolean getSaveStatusOfUserLogin(Long productId) {
        UserWishListResponse result = userWishListService.loadStatusFollows(productId);
        return result != null;
    }

    // TODO
    private List<ProductAttributeResponse> buildAttributeOption(Long productId) {
        return productAttributeService.findByProductId(productId);
    }

    // TODO
    private List<ProductPriceResponse> buildProductPrice(Long productId) {
        List<ProductPrice> productPriceList = productPriceService.getProductPricesByProduct(productId);
        return productPriceList.stream()
                .map(e -> {
                    ProductPriceResponse obj = BeanUtil.copyProperties(e, ProductPriceResponse.class);
                    obj.setProductAttributes(productAttributeService.findByProductPriceId(e.getId()));
                    return obj;
                }).collect(Collectors.toList());
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

    private ProductOwnerResponse buildNewFeedOwner(Long id, TargetType targetType) {
        final ProductOwnerResponse[] productOwnerResponse = new ProductOwnerResponse[1];
        if (targetType == TargetType.STORE) {
            productOwnerResponse[0] = productStoreService.getStoreOwner(id);
        } else {
            userService.fetchById(id).ifPresent(user ->
                    productOwnerResponse[0] = ProductOwnerResponse.builder()
                            .id(user.getId())
                            .name(user.getFullName())
                            .logoUrl(user.getAvatar())
                            .build());
        }
        return productOwnerResponse[0];
    }

    private StoreResponseLite getStoreResponseLite(Long storeId) {
        return Optional.ofNullable(storeId)
                .map(storeService::getStoreResponseLite)
                .orElse(null);
    }
}
