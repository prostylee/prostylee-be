package vn.prostylee.useractivity.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTrackingRequest {
    private Long categoryId;
    private Long productId;
    private Long storeId;
    private String searchKeyword;
    private String path;
}
