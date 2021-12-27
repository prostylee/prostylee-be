package vn.prostylee.product.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryResponseLite implements Serializable {
    private Long id;
    private String name;
    private String description;
}
