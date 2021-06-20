package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BuyAtType {
    ALL(0),
    STORE(1),
    WEBSITE(2),
    MOBILE_APP(2);

    private final int type;
}
