package vn.prostylee.product.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class BrandRequest {
    @NotEmpty
    @Size(max = 4096)
    private String description;

    @NotEmpty
    @Size(max = 512)
    private String name;

    private String icon;
}
