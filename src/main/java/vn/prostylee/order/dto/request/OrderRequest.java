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

    private String code;

    @NotNull
    @Min(1)
    private Double totalMoney;

    @NotNull
    @Schema(allowableValues = {"CREATE_ORDER", "RECEIVE_ORDER", "GOOD_ISSUE", "DELIVERY", "CANCEL_ORDER", "COMPLETE"})
    private String status;

    @NotNull
    private Long statusId;

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
