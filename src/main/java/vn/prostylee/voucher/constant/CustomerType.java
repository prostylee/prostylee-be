package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum CustomerType {

    ALL(0),
    LIMIT_LOCATION(1),
    LIMIT_MEMBER(2),
    LIMIT_POTENTIAL_CUSTOMER(3),
    LIMIT_NEW_CUSTOMER(4),
    LIMIT_SPECIFIC_CUSTOMER(5)
    ;

    private final int type;

    public static Optional<CustomerType> findByCustomerType(Integer customerType) {
        if (customerType == null) {
            return Optional.empty();
        }
        return Arrays.stream(CustomerType.values())
                .filter(item -> item.getType() == customerType)
                .findFirst();
    }
}
