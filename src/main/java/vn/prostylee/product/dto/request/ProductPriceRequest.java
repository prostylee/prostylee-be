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
public class ProductPriceRequest {

    @NotBlank
    private String name;

    @NotNull
    private Long productId;

    private String sku;

    @NotNull
    private Double price;

    private Double priceSale;

    private Set<ProductAttributeRequest> productAttributeRequests;
}
