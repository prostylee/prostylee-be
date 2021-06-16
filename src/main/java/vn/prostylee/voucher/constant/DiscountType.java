package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DiscountType {

    FIXED_AMOUNT(1),
    PERCENT(2);

    @Getter
    private final Integer type;
}
