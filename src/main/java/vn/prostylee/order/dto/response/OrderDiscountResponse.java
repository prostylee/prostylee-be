package vn.prostylee.order.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDiscountResponse implements Serializable {

    private Long id;

    private Long voucherId;

    private Double amount;

    private String description;
}
