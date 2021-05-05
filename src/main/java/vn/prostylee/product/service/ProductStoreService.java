package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.ProductOwnerResponse;

public interface ProductStoreService {

    ProductOwnerResponse getStoreOwner(Long storeId);
}
