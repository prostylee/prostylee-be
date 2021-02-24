package vn.prostylee.product.service;

import vn.prostylee.product.dto.request.ProductImageRequest;
import vn.prostylee.product.dto.response.ProductImageResponse;
import vn.prostylee.product.entity.Product;

import java.util.Set;

public interface ProductImageService {
    ProductImageResponse save(Set<ProductImageRequest> productImageRequests, Product product);
}
