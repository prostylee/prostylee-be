package vn.prostylee.product.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;

    private Long brandId;

    private Long categoryId;

    private String name;

    private String description;

    private String locationId;

    private Long storeId;

    private Double price;

    private Double priceSale;
}
