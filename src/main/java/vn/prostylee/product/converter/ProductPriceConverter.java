package vn.prostylee.product.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
@Slf4j
public class ProductPriceConverter {

    private final ProductAttributeConverter productAttributeConverter;

    public ProductPriceResponse toDto(ProductPrice productPrice) {
        ProductPriceResponse productPriceResponse = BeanUtil.copyProperties(productPrice, ProductPriceResponse.class);
        List<ProductAttributeResponse> attrs = new ArrayList<>();
        for (ProductAttribute attr : productPrice.getProductAttributes()) {
            attrs.add(productAttributeConverter.toDto(attr));
        }
        productPriceResponse.setProductAttributes(attrs);
        return productPriceResponse;
    }

    public void toEntity(ProductPriceRequest productPriceRequest, ProductPrice productPrice) {
        Product product = Product.builder().id(productPriceRequest.getProductId()).build();
        productPrice.setProduct(product);
        Set<ProductAttribute> productAttributes = new HashSet<>();
        for(ProductAttributeRequest request : productPriceRequest.getProductAttributeRequests()) {
            ProductAttribute productAttribute = BeanUtil.copyProperties(request, ProductAttribute.class);
            productAttributeConverter.toEntity(request, productAttribute);
            productAttributes.add(productAttribute);
        }
        productPrice.setProductAttributes(productAttributes);
    }
}
