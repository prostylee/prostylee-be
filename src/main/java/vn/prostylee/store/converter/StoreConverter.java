package vn.prostylee.store.converter;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.service.CategoryService;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.response.CompanyResponse;
import vn.prostylee.store.dto.response.StoreMiniResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.entity.Company;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.service.StoreBannerService;
import vn.prostylee.useractivity.dto.request.StatusFollowRequest;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreConverter {

    @Lazy private final ProductService productService;
    private final FileUploadService fileUploadService;
    private final LocationService locationService;
    private final StoreBannerService storeBannerService;
    private final UserFollowerService userFollowerService;
    private final CategoryService categoryService;

    public StoreResponse convertToResponse(Store store) {
        StoreResponse storeResponse = BeanUtil.copyProperties(store, StoreResponse.class);
        setStoreLogo(storeResponse, store.getLogo());
        setStoreLocation(storeResponse, store.getLocationId());
        storeResponse.setFollowStatusOfUserLogin(getFollowStatusOfUserLogin(store.getId()));
        Optional.ofNullable(store.getCompany()).ifPresent(company -> storeResponse.setCompanyId(company.getId()));
        return storeResponse;
    }

    public StoreResponse convertToFullResponse(Store store, int numberOfProducts) {
        StoreResponse storeResponse = BeanUtil.copyProperties(store, StoreResponse.class);
        return convertToFullResponse(store, storeResponse, numberOfProducts);
    }

    public StoreResponse convertToFullResponse(Store store, StoreResponse storeResponse, int numberOfProducts) {
        storeResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127

        setStoreLogo(storeResponse, store.getLogo());
        setStoreLocation(storeResponse, store.getLocationId());
        setStoreProducts(storeResponse, numberOfProducts);
        setStoreCompany(storeResponse, store.getCompany());
        setStoreBanner(storeResponse,store.getId());
        storeResponse.setCategoryResponseLites(categoryService.getCategoryResponseLite(store.getId()));
        storeResponse.setFollowStatusOfUserLogin(getFollowStatusOfUserLogin(store.getId()));
        return storeResponse;
    }

    private void setStoreBanner(StoreResponse storeResponse, Long storeId) {
        Optional.ofNullable(storeId)
                .ifPresent(com -> storeResponse.setStoreBannerResponses(storeBannerService.getListStoreBannerByStore(storeId)));
    }

    private void setStoreCompany(StoreResponse storeResponse, Company company) {
        Optional.ofNullable(company)
                .ifPresent(com -> storeResponse.setCompany(BeanUtil.copyProperties(com, CompanyResponse.class)));
    }

    private void setStoreLogo(StoreResponse storeResponse, Long logo) {
        storeResponse.setLogoUrl(getLogoUrl(logo));
    }

    private String getLogoUrl(Long logo) {
        if (logo == null) {
            return null;
        }
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(logo), ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            return imageUrls.get(0);
        }
        return null;
    }

    private void setStoreLocation(StoreResponse storeResponse, Long locationId) {
        if (locationId != null && storeResponse.getLocation() == null) {
            try {
                storeResponse.setLocation(locationService.findById(locationId));
            } catch (ResourceNotFoundException e) {
                log.debug("Could not found a location with id={}", locationId);
            }
        }
    }

    private void setStoreProducts(StoreResponse storeResponse, int numberOfProducts) {
        if (numberOfProducts > 0) {
            List<ProductResponseLite> products = getLatestProducts(storeResponse.getId(), numberOfProducts);
            storeResponse.setProducts(products);
        }
    }

    private List<ProductResponseLite> getLatestProducts(Long storeId, int numberOfProducts) {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setLimit(numberOfProducts);
        productFilter.setStoreId(storeId);
        productFilter.setSorts(new String[] {"-createdAt"});
        return productService.getProductsForListStore(productFilter).getContent();
    }

    public StoreMiniResponse convertToMiniResponse(StoreResponse storeResponse) {
        StoreMiniResponse storeMiniResponse = BeanUtil.copyProperties(storeResponse, StoreMiniResponse.class);
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(storeResponse.getLogo()), ImageSize.LOGO.getWidth(), ImageSize.LOGO.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeMiniResponse.setLogoUrl(imageUrls.get(0));
        }
        storeMiniResponse.setLocationLite(locationService.getLocationResponseLite(storeResponse.getLocationId()));
        return storeMiniResponse;
    }

    private Boolean getFollowStatusOfUserLogin(Long storeId) {
        StatusFollowRequest request = StatusFollowRequest.builder()
                .targetIds(Collections.singletonList(storeId))
                .targetType(TargetType.STORE).build();
        return !userFollowerService.loadStatusFollows(request).isEmpty();
    }
}
