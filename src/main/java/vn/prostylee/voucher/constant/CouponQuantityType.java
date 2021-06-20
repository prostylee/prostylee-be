package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponQuantityType {
    NONE(0),
    LIMIT(1);

    private final int type;
}
