package vn.prostylee.order.dto.response;

import lombok.Data;

@Data
public class VoucherCheckResultResponse {

    private Long voucherId;

    private Double amount;

    private Boolean result;
}
