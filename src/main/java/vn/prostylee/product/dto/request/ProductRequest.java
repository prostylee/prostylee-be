package vn.prostylee.product.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductRequest {

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    @NotBlank
    private String name;

    private String description;

    private String locationId;

    private Long storeId;

    @NotNull
    private Double price;

    private Double priceSale;
}
