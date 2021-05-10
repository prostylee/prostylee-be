package vn.prostylee.product.dto.response;

import lombok.Data;

@Data
public class ProductStatisticResponse {

    private Long id;

    private Long numberOfSold;

    private Long numberOfLike;

    private Long numberOfComment;

    private Double resultOfRating;

    private Long numberOfReview;
}
