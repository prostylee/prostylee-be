package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeOptionResponse {
    private Long id;

    private String key;

    private Integer order;

    private String label;

    private String description;

    private Integer type;

    private String languageCode;

    private List<ProductAttributeResponse> productAttributeResponses;
}
