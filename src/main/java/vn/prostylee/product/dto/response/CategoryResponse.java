package vn.prostylee.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
public class CategoryResponse {
    private Long id;
    private Long parentId;
    private Long attachmentId;
    private String name;
    private String description;
    private Integer order;
    private String icon;
    private Boolean active;
    private String languageCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<AttributeResponse> attributes;
}
