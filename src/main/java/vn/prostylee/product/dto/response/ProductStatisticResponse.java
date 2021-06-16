package vn.prostylee.product.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductStatisticResponse implements Serializable {

    private Long id;

    private Long numberOfSold;

    private Long numberOfLike;

    private Long numberOfComment;

    private Double resultOfRating;

    private Long numberOfReview;
}
