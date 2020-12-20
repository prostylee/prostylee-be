package vn.prostylee.product.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttributeOptionRequest {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String value;
    @NotBlank
    private String label;
}
