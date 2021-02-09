package vn.prostylee.product.dto.response;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AttributeResponse {

    private Long id;
    @NotBlank
    private String key;

    private Integer order;

    @NotBlank
    private String label;

    private String description;

    @NotNull
    private Integer type;

    private String languageCode;

    @NotNull
    private Set<AttributeOptionResponse> attributeOptions;
}
