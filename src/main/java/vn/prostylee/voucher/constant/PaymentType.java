package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {
    ALL(0),
    ONLINE(1),
    OFFLINE(2);

    private final int type;
}
