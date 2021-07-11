package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseLite implements Serializable {
    private Long id;
    private Long categoryId;
    private String imageUrl;
    private String name;
    private Double price;
    private Double priceSale;
    private Boolean likeStatusOfUserLogin;
    private Boolean reviewedStatusOfUserLogin;
}
