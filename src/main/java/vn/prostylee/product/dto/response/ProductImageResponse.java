package vn.prostylee.product.dto.response;

import lombok.Builder;
import lombok.Data;
import vn.prostylee.product.entity.ProductImage;

import java.util.Set;

@Data
@Builder
public class ProductImageResponse {
    private Set<ProductImage> productImages;
}
