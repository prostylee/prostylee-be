package vn.prostylee.order.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    CREATE_ORDER(0),
    RECEIVE_ORDER(10),
    GOOD_ISSUE(20),
    DELIVERY(30),
    CANCEL_ORDER(90),
    COMPLETED(100);

    private final int status;

    public static OrderStatus getByStatusValue(String status) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> StringUtils.equalsIgnoreCase(storeStatus.name(), status))
                .findFirst()
                .orElse(CREATE_ORDER);
    }

    public static OrderStatus parse(int statusValue) {
        return Stream.of(OrderStatus.values())
                .filter(storeStatus -> storeStatus.getStatus() == statusValue)
                .findFirst()
                .orElse(null);
    }
}
