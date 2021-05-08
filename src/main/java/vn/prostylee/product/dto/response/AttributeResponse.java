package vn.prostylee.product.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class AttributeResponse {

    private Long id;

    private String key;

    private Integer order;

    private String label;

    private String description;

    private Integer type;

    private String languageCode;

    private Set<AttributeOptionResponse> attributeOptions;
}
