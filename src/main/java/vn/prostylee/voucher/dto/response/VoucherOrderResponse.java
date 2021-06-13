package vn.prostylee.voucher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VoucherOrderResponse {

    private Double amount;

    private Double discountAmount;

    private Double balance;

    private Long shippingProviderId;

    private Long shippingMethodId;

    private Long paymentTypeId;

    private Long buyerId;
}
