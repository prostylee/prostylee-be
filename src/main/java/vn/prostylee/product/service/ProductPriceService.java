package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceResponse;

public interface ProductPriceService extends CrudService<ProductPriceRequest, ProductPriceResponse, Long> {
}
