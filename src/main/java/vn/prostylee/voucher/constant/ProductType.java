package vn.prostylee.voucher.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum ProductType {
    ALL(0),
    SPECIFIC_PRODUCT(1),
    SPECIFIC_CATEGORY(2),
    NONE(3),
    ;

    private final int type;

    public static Optional<ProductType> findByProductType(Integer productType) {
        if (productType == null) {
            return Optional.empty();
        }
        return Arrays.stream(ProductType.values())
                .filter(item -> item.getType() == productType)
                .findFirst();
    }
}
