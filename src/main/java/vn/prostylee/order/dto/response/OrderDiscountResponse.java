package vn.prostylee.order.dto.response;

import lombok.Data;

@Data
public class OrderDiscountResponse {

    private Long id;

    private Long voucherId;

    private Double amount;

    private String description;
}
