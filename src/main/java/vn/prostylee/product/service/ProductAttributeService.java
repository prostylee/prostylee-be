package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.ProductAttribute;

public interface ProductAttributeService extends CrudService<ProductAttributeRequest, ProductAttributeResponse, Long> {
    ProductAttributeResponse toDto(ProductAttribute productAttribute);
}
