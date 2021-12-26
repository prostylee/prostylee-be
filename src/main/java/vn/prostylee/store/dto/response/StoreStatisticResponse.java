package vn.prostylee.store.dto.response;

import lombok.Data;

@Data
public class StoreStatisticResponse {
    private Long numberOfFollower;
    private Long numberOfFollowing;
    private Long numberOfProduct;
    private Long numberOfPost;
    private Long numberOfRating;
    private Long numberOfComment;

}
