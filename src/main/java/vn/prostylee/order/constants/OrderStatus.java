package vn.prostylee.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum OrderStatus {

    AWAITING_CONFIRMATION(0),
    IN_PROGRESS(1),
    COMPLETE(2);
 
    @Getter
    private final int value;

    public static final OrderStatus getByStatusValue(int value) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> storeStatus.getValue() == value)
                .findFirst()
                .orElse(AWAITING_CONFIRMATION);

    }
}
