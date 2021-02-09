package vn.prostylee.product.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductStatus {
    DRAFT(0),
    WAITING_FOR_APPROVE(50),
    PUBLISHED(100),
    CLOSED(1000);

    private final int status;
}
