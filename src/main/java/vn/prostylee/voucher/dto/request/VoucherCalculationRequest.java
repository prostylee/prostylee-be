package vn.prostylee.voucher.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VoucherCalculationRequest {

    @NotNull
    private Long voucherId;

    @Valid
    @NotNull
    private VoucherOrderRequest order;

    @NotEmpty
    @Valid
    private List<VoucherOrderDetailRequest> orderDetails;
}
