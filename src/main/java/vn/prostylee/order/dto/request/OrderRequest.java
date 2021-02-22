package vn.prostylee.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotEmpty
    private String code;

    @NotNull
    @Min(1)
    private Double totalMoney;

    @NotNull
    private String status;

    @NotNull
    private Long paymentTypeId;

    @NotNull
    private Long buyerId;

    @NotNull
    private Long shippingAddressId;

    @NotNull
    private Long shippingProviderId;

    @NotEmpty
    private List<OrderDetailRequest> orderDetails;

    @NotNull
    private List<OrderDiscountRequest> orderDiscounts;
}
