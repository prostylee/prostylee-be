package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.story.dto.response.StoreForStoryResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductForStoryResponse {
    private Long id;
    private String name;
    private Double price;
    private Double priceSale;
    private List<String> imageUrls;
    private StoreForStoryResponse storeForStoryResponse;
}
