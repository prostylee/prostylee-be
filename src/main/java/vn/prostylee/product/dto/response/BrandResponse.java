package vn.prostylee.product.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandResponse implements Serializable {
    private Long id;
    private String description;
    private String name;
    private String icon;
    private Integer order;
}
