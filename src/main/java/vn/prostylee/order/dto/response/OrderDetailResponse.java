package vn.prostylee.order.dto.response;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderDetailResponse {

    private Long id;

    @NotNull
    private Long productId;

    private Double productPrice;

    @NotNull
    @Min(1)
    private Integer amount;

    private String productName;

    private String productImage;

    private String productColor;

    private String productSize;

    private String productData;

}
