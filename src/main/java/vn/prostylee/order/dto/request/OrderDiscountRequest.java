package vn.prostylee.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDiscountRequest {

    @NotNull
    private Long voucherId;

    @NotNull
    @Min(0)
    private Double amount;

    private String description;
}
