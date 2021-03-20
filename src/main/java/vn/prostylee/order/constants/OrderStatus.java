package vn.prostylee.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum OrderStatus {

    AWAITING_CONFIRMATION(0),
    IN_PROGRESS(50),
    CANCELLED(90),
    COMPLETED(100);

    @Getter
    private final int value;

    public static final OrderStatus getByStatusValue(String status) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> storeStatus.name().trim().equalsIgnoreCase(status))
                .findFirst()
                .orElse(AWAITING_CONFIRMATION);
    }
}
