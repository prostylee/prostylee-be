package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTrackingResponse implements Serializable {
    private Long productId;
    private Long storeId;
    private Long categoryId;
    private String searchKeyword;
    private String path;
}
