package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserTrackingResponse {
    private Long createdBy;
    private Date createdAt;
    private Long productId;
    private Long storeId;
    private Long categoryId;
    private String searchKeyword;
    private String path;
}
