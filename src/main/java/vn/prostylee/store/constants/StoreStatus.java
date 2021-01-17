package vn.prostylee.store.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum StoreStatus {

    INACTIVE(0),
    ACTIVE(1),
    IN_PROGRESS(2);

    @Getter
    private final int value;

    public static final StoreStatus getByStatusValue(int value) {
        return Stream.of(StoreStatus.values())
                .filter(storeStatus -> storeStatus.getValue() == value)
                .findFirst()
                .orElse(null);

    }
}
