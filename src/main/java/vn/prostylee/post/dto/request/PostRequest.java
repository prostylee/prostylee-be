package vn.prostylee.post.dto.request;

import lombok.Data;
import vn.prostylee.product.dto.request.ProductImageRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostRequest {

    @Size(max = 4096)
    private String description;

    @NotNull
    private Long storeId;

    @NotEmpty
    private List<ProductImageRequest> productImageRequests;
}
