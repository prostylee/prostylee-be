package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ShippingMethodType {
    NONE(0),
    ALL(1),
    SPECIFIC(2);

    private final int type;
}
