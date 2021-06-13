package vn.prostylee.voucher.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoucherOrderRequest {

    private Double amount;

    private Long shippingProviderId;

    private Long shippingMethodId;

    private Long paymentTypeId;

    @NotNull
    private Long buyerId;
}
