package vn.prostylee.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.location.dto.request.LocationRequest;

import javax.validation.constraints.*;
import java.util.List;
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

    @NotNull
    private Long usedStatusId;

    @NotNull
    private Long storeId;

    @NotBlank
    @Size(max = 512)
    private String name;

    @NotBlank
    @Size(max = 4096)
    private String description;

    private LocationRequest locationRequest;

    @NotEmpty
    private List<Long> paymentTypes;

    @NotEmpty
    private List<Long> shippingProviders;

    @NotNull
    private Double price;

    private Double priceSale;

    @NotEmpty
    private List<ProductImageRequest> productImageRequests;
}
