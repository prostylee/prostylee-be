package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.location.dto.response.LocationResponse;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {

    private Long id;

    private Long brandId;

    private Long categoryId;

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

    private Boolean likeStatusOfUserLogin;

    private ProductStatisticResponse productStatisticResponse;

    private CategoryResponse categoryResponse;

    private BrandResponse brandResponse;

    private List<ProductAttributeResponse> productAttributeOptionResponse; // TODO rename

    private List<ProductPriceResponse> productPriceResponseList;

    private Boolean saveStatusOfUserLogin;
}
