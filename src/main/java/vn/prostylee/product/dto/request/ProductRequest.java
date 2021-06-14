package vn.prostylee.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.location.dto.request.LocationRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotNull
    private Long brandId;

    @NotNull
    private Long categoryId;

    private Long storeId;

    @NotBlank
    @Size(max = 512)
    private String name;

    @NotBlank
    @Size(max = 4096)
    private String description;

    @NotNull
    private Double price;

    private Double priceSale;

    private LocationRequest locationRequest;

    @NotNull
    private List<ProductPriceRequest> productPriceRequest;

    @NotEmpty
    private List<Long> paymentTypes;

    @NotEmpty
    private List<Long> shippingProviders;

    @NotEmpty
    private List<ProductImageRequest> productImageRequests;
}
