package vn.prostylee.order.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    AWAITING_CONFIRMATION(0),
    IN_PROGRESS(50),
    BUY_AT_STORE(70),
    CANCELLED(90),
    COMPLETED(100);

    private final int status;

    public static OrderStatus getByStatusValue(String status) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> StringUtils.equalsIgnoreCase(storeStatus.name(), status))
                .findFirst()
                .orElse(AWAITING_CONFIRMATION);
    }

    public static OrderStatus parse(int statusValue) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> storeStatus.getStatus() == statusValue)
                .findFirst()
                .orElse(null);
    }
}
