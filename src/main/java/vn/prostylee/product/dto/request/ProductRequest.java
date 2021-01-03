package vn.prostylee.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private Set<ProductImageRequest> productImageRequests;
}
