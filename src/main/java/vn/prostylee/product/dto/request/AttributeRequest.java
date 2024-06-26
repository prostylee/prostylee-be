package vn.prostylee.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Boolean allowsMultipleSelection;

    private String languageCode;

    @NotNull
    private Set<AttributeOptionRequest> attributeOptions;
}
