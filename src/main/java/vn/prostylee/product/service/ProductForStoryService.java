package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.ProductForStoryResponse;

public interface ProductForStoryService {
    ProductForStoryResponse getProductForStory(Long productId);
}
