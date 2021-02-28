package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;

public interface ProductAttributeService extends CrudService<ProductAttributeRequest, ProductAttributeResponse, Long> {
}
