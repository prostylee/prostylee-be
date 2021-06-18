package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.ProductAttribute;

import java.util.List;

public interface ProductAttributeService extends CrudService<ProductAttributeRequest, ProductAttributeResponse, Long> {

    List<ProductAttribute> getProductAttributeByProductId(Long productId);

    List<ProductAttribute> getProductAttributeByPriceId(Long priceId);

    List<ProductAttribute> getProductAttributeByIds(List<Long> productAttrs);
}
