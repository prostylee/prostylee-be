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

@Component
@AllArgsConstructor
@Slf4j
public class ProductAttributeConverter {

    public ProductAttributeResponse toDto(ProductAttribute productAttribute) {
        return BeanUtil.copyProperties(productAttribute, ProductAttributeResponse.class);
    }

    public void toEntity(ProductAttributeRequest request, ProductAttribute productAttribute) {
        Attribute attribute = Attribute.builder().id(request.getAttributeId()).build();
        ProductPrice productPrice = ProductPrice.builder().id(request.getProductPriceId()).build();
        productAttribute.setAttribute(attribute);
        productAttribute.setProductPrice(productPrice);
    }
}
