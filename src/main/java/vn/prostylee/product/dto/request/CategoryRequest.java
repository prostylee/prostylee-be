package vn.prostylee.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank
    private String name;

    private String description;

    private Integer order;

    private String icon;

    private Boolean active;

    private String languageCode;

    private Set<AttributeRequest> attributes;
}
