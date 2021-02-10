package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.response.ProductForStoryResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductForStoryService;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.story.dto.response.StoreForStoryResponse;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductForStoryServiceImpl implements ProductForStoryService {

    public static final int FIRST_INDEX = 0;
    private final ProductService productService;
    private final StoreService storeService;
    private final FileUploadService fileUploadService;

    public ProductForStoryResponse getProductForStory(Long productId) {
        ProductResponse productResponse = productService.findById(productId);
        ProductForStoryResponse productForStoryResponse = BeanUtil.copyProperties(productResponse, ProductForStoryResponse.class);
        productForStoryResponse.setStoreForStoryResponse(this.fetchStoreForStory(productResponse));
        return productForStoryResponse;
    }

    private StoreForStoryResponse fetchStoreForStory(ProductResponse productResponse) {
        StoreResponse storeResponse = storeService.findById(productResponse.getStoreId());
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(storeResponse.getLogo()),
                ImageSize.SMALL.getWidth(), ImageSize.SMALL.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeResponse.setLogoUrl(imageUrls.get(FIRST_INDEX));
        }
        return BeanUtil.copyProperties(storeResponse, StoreForStoryResponse.class);
    }
}
