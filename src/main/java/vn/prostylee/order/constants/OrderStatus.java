package vn.prostylee.order.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum OrderStatus {

    AWAITING_CONFIRMATION("AWAITING_CONFIRMATION"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETE("COMPLETE");

    @Getter
    private final String value;

    public static final OrderStatus getByStatusValue(String value) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> storeStatus.getValue().equals(value))
                .findFirst()
                .orElse(AWAITING_CONFIRMATION);

    }
}
