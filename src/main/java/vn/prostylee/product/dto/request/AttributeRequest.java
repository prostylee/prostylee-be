package vn.prostylee.product.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class AttributeRequest {

    private Long id;

    @NotBlank
    private String key;

    private Integer order;

    @NotBlank
    private String label;

    private String description;

    @NotNull
    private Integer type;

    @NotNull
    private Set<AttributeOptionRequest> attributeOptions;
}
