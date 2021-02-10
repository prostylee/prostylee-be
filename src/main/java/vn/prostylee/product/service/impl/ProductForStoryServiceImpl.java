package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.response.ProductForStoryResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductForStoryService;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.story.dto.response.StoreForStoryResponse;

@Service
@RequiredArgsConstructor
public class ProductForStoryServiceImpl implements ProductForStoryService {

    private final ProductService productService;
    private final StoreService storeService;

    public ProductForStoryResponse getProductForStory(Long productId) {
        ProductResponse productResponse = productService.findById(productId);
        ProductForStoryResponse productForStoryResponse = BeanUtil.copyProperties(productResponse, ProductForStoryResponse.class);
        productForStoryResponse.setStoreForStoryResponse(this.fetchStoreForStory(productResponse));
        return productForStoryResponse;
    }

    private StoreForStoryResponse fetchStoreForStory(ProductResponse productResponse) {
        StoreResponse storeResponse = storeService.findById(productResponse.getStoreId());
        return BeanUtil.copyProperties(storeResponse, StoreForStoryResponse.class);
    }
}
