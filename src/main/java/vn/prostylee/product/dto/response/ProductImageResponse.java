package vn.prostylee.product.dto.response;

import lombok.Builder;
import lombok.Data;
import vn.prostylee.product.entity.ProductImage;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
public class ProductImageResponse implements Serializable {
    private Set<ProductImage> productImages;
}
