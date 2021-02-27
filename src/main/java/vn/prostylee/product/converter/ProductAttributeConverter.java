package vn.prostylee.product.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.service.AttributeService;
import vn.prostylee.product.service.ProductPriceService;

@Component
@AllArgsConstructor
@Slf4j
public class ProductAttributeConverter {

    private final ProductPriceService productPriceService;
    private final AttributeService attributeService;

    public ProductAttributeResponse toDto(ProductAttribute productAttribute) {
        return BeanUtil.copyProperties(productAttribute, ProductAttributeResponse.class);
    }

    public void toEntity(ProductAttributeRequest request, ProductAttribute productAttribute) {
        Attribute attribute = attributeService.getAttributeById(request.getAttributeId());
        ProductPrice productPrice = productPriceService.getProductPriceById(request.getProductPriceId());
        productAttribute.setAttribute(attribute);
        productAttribute.setProductPrice(productPrice);
    }
}
