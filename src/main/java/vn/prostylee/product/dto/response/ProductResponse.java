package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.location.dto.response.LocationResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private Long brandId;

    private Long categoryId;

    private Long usedStatusId;

    private String name;

    private String description;

    private Long locationId;

    private Long storeId;

    private Double price;

    private Double priceSale;

    private List<String> imageUrls;

    private ProductOwnerResponse productOwnerResponse;

    private LocationResponse location;

    private Boolean isAdvertising;
}
