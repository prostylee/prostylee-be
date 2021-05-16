package vn.prostylee.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.shipping.dto.request.ShippingAddressRequest;

import javax.validation.Valid;
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
    @Schema(allowableValues = {"AWAITING_CONFIRMATION", "IN_PROGRESS", "BUY_AT_STORE", "COMPLETE"})
    private String status;

    private Long paymentTypeId;

    @NotNull
    private Long buyerId;

    @Valid
    private ShippingAddressRequest shippingAddress;

    private Long shippingProviderId;

    @Valid
    @NotEmpty
    private List<OrderDetailRequest> orderDetails;

    @Valid
    private List<OrderDiscountRequest> orderDiscounts;
}
