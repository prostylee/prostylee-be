package vn.prostylee.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {

    @NotNull
    private Long storeId;

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
