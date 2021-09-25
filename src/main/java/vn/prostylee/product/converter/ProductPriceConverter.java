package vn.prostylee.product.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class ProductPriceConverter {

    private final ProductAttributeConverter productAttributeConverter;

    public ProductPriceResponse toDto(ProductPrice productPrice) {
        ProductPriceResponse productPriceResponse = BeanUtil.copyProperties(productPrice, ProductPriceResponse.class);
        if (productPrice.getProductAttributes() == null) {
            return productPriceResponse;
        }
        productPriceResponse.setProductAttributes(productAttributeConverter.toResponse(productPrice.getProductAttributes()));
        return productPriceResponse;
    }

    public void toEntity(ProductPriceRequest productPriceRequest, ProductPrice productPrice) {
        Product product = Product.builder().id(productPriceRequest.getProductId()).build();
        productPrice.setProduct(product);
        if (productPriceRequest.getProductAttributes() == null) {
            return;
        }
        Set<ProductAttribute> productAttributes = new HashSet<>();
        for (ProductAttributeRequest request : productPriceRequest.getProductAttributes()) {
            ProductAttribute productAttribute = BeanUtil.copyProperties(request, ProductAttribute.class);
            productAttributeConverter.toEntity(request, productAttribute, productPrice);
            productAttributes.add(productAttribute);
        }
        Set<ProductAttribute> entityProductAttrs = productPrice.getProductAttributes();
        if (entityProductAttrs == null) {
            productPrice.setProductAttributes(productAttributes);
            return;
        }
        entityProductAttrs.clear();
        entityProductAttrs.addAll(productAttributes);
    }
}
