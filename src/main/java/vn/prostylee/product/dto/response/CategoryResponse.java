package vn.prostylee.product.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Integer order;
    private String icon;
    private Boolean active;
    private Set<AttributeResponse> attributes;
}
