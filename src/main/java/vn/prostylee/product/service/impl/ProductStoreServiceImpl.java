package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.response.ProductOwnerResponse;
import vn.prostylee.product.service.ProductStoreService;
import vn.prostylee.store.repository.StoreRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductStoreServiceImpl implements ProductStoreService {

    private final StoreRepository storeRepository;
    private final FileUploadService fileUploadService;

    @Cacheable(value = CachingKey.STORES, key = "#storeId")
    @Override
    public ProductOwnerResponse getStoreOwner(Long storeId) {
        return storeRepository.findById(storeId)
                .map(store -> {
                    String logoUrl = null;
                    if (store.getLogo() != null) {
                        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(store.getLogo()), ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
                        if (CollectionUtils.isNotEmpty(imageUrls)) {
                            logoUrl = imageUrls.get(0);
                        }
                    }
                    return ProductOwnerResponse.builder()
                            .id(store.getId())
                            .name(store.getName())
                            .logoUrl(logoUrl)
                            .build();
                }).orElse(null);
    }
}
