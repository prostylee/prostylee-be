package vn.prostylee.useractivity.dto.response;

import lombok.Data;

@Data
public class UserTrackingResponse {
    private Long productId;
    private Long storeId;
    private Long categoryId;
    private String searchKeyword;
    private String path;
}
