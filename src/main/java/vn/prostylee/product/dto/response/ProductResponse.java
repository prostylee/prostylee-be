package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    private List<String> imageUrls;
}
