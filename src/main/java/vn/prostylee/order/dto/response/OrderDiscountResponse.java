package vn.prostylee.order.dto.response;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderDiscountResponse {

    private Long id;

    @NotNull
    private Long voucherId;

    @NotNull
    @Min(0)
    private Double amount;

    private String description;
}
