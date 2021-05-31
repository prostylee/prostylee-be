package vn.prostylee.useractivity.dto.response;

import lombok.Data;
import vn.prostylee.product.dto.response.ProductResponseLite;

@Data
public class UserWishListResponse {

    private Long id;

    private ProductResponseLite productResponseLite;

}
