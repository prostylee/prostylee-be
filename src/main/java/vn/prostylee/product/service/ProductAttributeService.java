package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.ProductAttribute;

import java.util.List;

public interface ProductAttributeService {

    List<ProductAttributeResponse> findByProductId(Long productId);

    List<ProductAttributeResponse> findByProductPriceId(Long priceId);

    List<ProductAttribute> findByIds(List<Long> productAttrs);
}
