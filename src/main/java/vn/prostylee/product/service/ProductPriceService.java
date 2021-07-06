package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceRangeResponse;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.ProductPrice;

import java.util.List;

public interface ProductPriceService extends CrudService<ProductPriceRequest, ProductPriceResponse, Long> {
    ProductPrice getProductPriceById(Long id);

    List<ProductPrice> getProductPricesByProduct(Long productId);

    ProductPriceRangeResponse getProductPriceRange();
}
