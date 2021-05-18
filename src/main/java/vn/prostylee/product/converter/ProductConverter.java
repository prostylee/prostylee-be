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
import vn.prostylee.product.dto.response.ProductOwnerResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductStatisticResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductImage;
import vn.prostylee.product.service.ProductImageService;
import vn.prostylee.product.service.ProductStatisticService;
import vn.prostylee.product.service.ProductStoreService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public ProductResponse toResponse(Product product) {
        ProductResponse productResponse = BeanUtil.copyProperties(product, ProductResponse.class);
        productResponse.setImageUrls(buildImageUrls(product.getId()));
        productResponse.setLocation(buildLocation(productResponse.getLocationId()));
        productResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
        productResponse.setProductOwnerResponse(buildProductOwner(product));
        productResponse.setProductStatisticResponse(buildProductStatistic(product.getId()));
        return productResponse;
    }

    private List<String> buildImageUrls(long productID) {
        List<Long> attachmentIds = Optional.ofNullable(productID)
                .map(productImageService::getAttachmentIdByProductID)
                .orElse(null);
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
}
