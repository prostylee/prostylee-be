package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.ProductForStoryResponse;

public interface ProductForStoryService {
    public ProductForStoryResponse getProductForStory(Long productId);
}
