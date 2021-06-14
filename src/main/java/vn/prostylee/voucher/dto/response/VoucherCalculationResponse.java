package vn.prostylee.voucher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VoucherCalculationResponse {

    private Long voucherId;

    private String code;

    private Date expiredDate;

    private VoucherOrderResponse order;

    private List<VoucherOrderDetailResponse> orderDetails;
}
