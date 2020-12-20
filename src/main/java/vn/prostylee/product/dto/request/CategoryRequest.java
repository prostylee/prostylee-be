package vn.prostylee.product.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class CategoryRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Integer order;
    private String icon;
    private Boolean active;
    private Set<AttributeRequest> attributes;
}
