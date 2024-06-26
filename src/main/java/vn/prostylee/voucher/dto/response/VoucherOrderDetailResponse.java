package vn.prostylee.voucher.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class VoucherOrderDetailResponse {

    private Long storeId;

    private Long categoryId;

    private Long productId;

    private Integer quantity;

    private Double price;

    private Double amount;

    private Double discountAmount;

    private Double balance;
}
