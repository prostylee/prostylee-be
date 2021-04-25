package vn.prostylee.order.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    AWAITING_CONFIRMATION(0),
    IN_PROGRESS(50),
    CANCELLED(90),
    COMPLETED(100);

    private final int status;

    public static OrderStatus getByStatusValue(String status) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> storeStatus.name().trim().equalsIgnoreCase(status))
                .findFirst()
                .orElse(AWAITING_CONFIRMATION);
    }

    public static OrderStatus parse(int statusValue) {
        OrderStatus status = null; // Default
        for (OrderStatus item : OrderStatus.values()) {
            if (item.getStatus()==statusValue) {
                status = item;
                break;
            }
        }
        return status;
    }
}
