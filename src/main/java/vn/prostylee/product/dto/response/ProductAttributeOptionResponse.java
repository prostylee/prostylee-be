package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeOptionResponse implements Serializable {
    private Long id;
    private String name;
    private String attrValue;
    private String label;
    private String languageCode;
}
