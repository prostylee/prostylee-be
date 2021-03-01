package vn.prostylee.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
public class AttributeResponse {

    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long categoryId;

    private String key;

    private Integer order;

    private String label;

    private String description;

    private Integer type;

    private String languageCode;

    private Set<AttributeOptionResponse> attributeOptions;
}
