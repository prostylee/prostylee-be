package vn.prostylee.voucher.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VoucherUserRequest {

    @NotNull
    private Long voucherId;

    @NotBlank
    @Length(max = 20)
    private String voucherCode;

    @NotNull
    private Long storeId;

    private Long orderId;

    private Date usedAt;

    private Double discountAmount;
}
