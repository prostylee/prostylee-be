package vn.prostylee.product.converter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.AttributeOptionResponse;
import vn.prostylee.product.dto.response.AttributeResponse;
import vn.prostylee.product.dto.response.ProductAttributeOptionResponse;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.service.AttributeService;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class ProductAttributeConverter {

    private final AttributeService attributeService;

    public List<ProductAttributeResponse> toResponse(Collection<ProductAttribute> productAttributes) {
        if (CollectionUtils.isEmpty(productAttributes)) {
            return new ArrayList<>();
        }
        productAttributes = productAttributes.stream()
                .filter(productAttribute -> productAttribute.getAttribute() != null)
                .collect(Collectors.toList());

        Set<Long> attributeIds = productAttributes.stream()
                .map(ProductAttribute::getAttribute)
                .filter(Objects::nonNull)
                .map(Attribute::getId)
                .collect(Collectors.toSet());

        Map<Long, Set<String>> productAttributeValues = new HashMap<>();
        productAttributes.forEach(productAttribute -> {
            if (!productAttributeValues.containsKey(productAttribute.getAttribute().getId())) {
                productAttributeValues.put(productAttribute.getAttribute().getId(), new HashSet<>());
            }
            productAttributeValues.get(productAttribute.getAttribute().getId()).add(productAttribute.getAttrValue());
        });

        List<AttributeResponse> attributeResponses = attributeService.findByIdIn(attributeIds);
        if (CollectionUtils.isNotEmpty(attributeResponses)) {
            attributeResponses.forEach(attributeResponse -> {
                Set<String> values = productAttributeValues.get(attributeResponse.getId());
                List<AttributeOptionResponse> attributeOptions = attributeResponse.getAttributeOptions()
                        .stream()
                        .filter(attributeOptionResponse -> values.contains(attributeOptionResponse.getValue()))
                        .collect(Collectors.toList());
                attributeResponse.setAttributeOptions(attributeOptions);
            });
        }

        return attributeResponses.stream()
                .map(attributeResponse -> {
                    List<ProductAttributeOptionResponse> productAttributeOptionResponses = attributeResponse.getAttributeOptions()
                            .stream()
                            .map(attributeOptionResponse -> {
                                ProductAttributeOptionResponse productAttributeOptionResponse = BeanUtil.copyProperties(attributeResponse, ProductAttributeOptionResponse.class);
                                productAttributeOptionResponse.setAttrValue(attributeOptionResponse.getValue());
                                return productAttributeOptionResponse;
                            })
                            .collect(Collectors.toList());

                    ProductAttributeResponse productAttributeResponse = BeanUtil.copyProperties(attributeResponse, ProductAttributeResponse.class);
                    productAttributeResponse.setProductAttributeResponses(productAttributeOptionResponses);
                    return productAttributeResponse;
                })
                .collect(Collectors.toList());
    }

    public void toEntity(ProductAttributeRequest request, ProductAttribute productAttribute, ProductPrice productPrice) {
        Attribute attribute = Attribute.builder().id(request.getAttributeId()).build();
        productAttribute.setAttribute(attribute);
        productAttribute.setProductPrice(productPrice);
        productAttribute.setProduct(productPrice.getProduct());
    }
}
