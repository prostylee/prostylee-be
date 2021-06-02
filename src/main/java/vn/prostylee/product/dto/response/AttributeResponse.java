package vn.prostylee.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AttributeResponse implements Serializable {

    private Long id;

    private String key;

    private Integer order;

    private String label;

    private String description;

    @Schema(description = "block selector type : 1; checkbox : 2; radio : 3; ")
    private Integer type;

    @Schema(description = "true : allows multiple select in search screen; false : not allows")
    private Boolean allowsMultipleSelection;

    private String languageCode;

    private List<AttributeOptionResponse> attributeOptions;
}
