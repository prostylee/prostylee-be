package vn.prostylee.product.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.service.ProductAttributeService;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ProductPriceConverter {

    private final ProductAttributeService productAttributeService;

    public ProductPriceResponse toDto(ProductPrice productPrice) {
        ProductPriceResponse productPriceResponse = BeanUtil.copyProperties(productPrice, ProductPriceResponse.class);
        List<ProductAttributeResponse> attrs = new ArrayList<>();
        for (ProductAttribute attr : productPrice.getProductAttributes()) {
            attrs.add(productAttributeService.toDto(attr));
        }
        productPriceResponse.setProductAttributes(attrs);
        return productPriceResponse;
    }
}
