package vn.prostylee.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class CategoryResponse implements Serializable {
    private Long id;
    private Long parentId;
    private Long attachmentId;
    private String name;
    private String description;
    private Integer order;
    private String icon;
    private Boolean active;
    private String languageCode;
    private Boolean hotStatus;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<AttributeResponse> attributes;
    private String banner;
    private String backgroundButton;
}
