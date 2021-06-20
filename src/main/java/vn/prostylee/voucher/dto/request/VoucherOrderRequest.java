package vn.prostylee.voucher.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;

@Data
public class VoucherOrderRequest {

    private Double amount;

    private Long shippingProviderId;

    private Long shippingMethodId;

    @Valid
    private VoucherShippingAddressRequest shippingAddress;

    private Long paymentTypeId;

    private Long buyerId;

    @Schema(allowableValues = "ALL(0), STORE(1), WEBSITE(2), MOBILE_APP(2)")
    private Long buyAt;
}
