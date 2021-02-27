package vn.prostylee.product.service;

import vn.prostylee.product.dto.request.ProductImageRequest;
import vn.prostylee.product.dto.response.ProductImageResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductImage;

import java.util.List;
import java.util.Set;

public interface ProductImageService {
    Set<ProductImage> buildProductImages(List<ProductImageRequest> productImageRequests, Product product);
}
