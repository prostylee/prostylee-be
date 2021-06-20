package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderAmountType {

    NONE(0),
    LIMIT_MIN(1);

    private final int type;
}
