package vn.prostylee.voucher.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class VoucherCalculationRequest {

    private Long voucherId;

    @Valid
    @NotNull
    private VoucherOrderRequest order;

    @NotEmpty
    @Valid
    private List<VoucherOrderDetailRequest> orderDetails;

    @JsonIgnore
    public List<Long> getProductIds() {
        return Optional.ofNullable(orderDetails)
                .orElseGet(Collections::emptyList).stream()
                .map(VoucherOrderDetailRequest::getProductId)
                .collect(Collectors.toList());
    }
}
