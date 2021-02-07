package vn.prostylee.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private String code;

    @NotNull
    @Min(1)
    private Double totalMoney;

    private Integer status;

    @NotNull
    private Long buyerId;

    @NotNull
    private Long shippingAddressId;

    @NotNull
    private Long shippingProviderId;

    private List<OrderDetailRequest> orderDetails;

    private List<OrderDiscountRequest> orderDiscounts;
}
