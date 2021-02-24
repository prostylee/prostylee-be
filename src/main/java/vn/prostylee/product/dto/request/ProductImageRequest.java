package vn.prostylee.product.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductImageRequest {
    private String name;
    private String path;

}
