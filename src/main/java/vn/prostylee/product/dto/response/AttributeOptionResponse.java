package vn.prostylee.product.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AttributeOptionResponse implements Serializable {
    private Long id;
    private String name;
    private String value;
    private String label;
    private String languageCode;
}
