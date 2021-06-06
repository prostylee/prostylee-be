package vn.prostylee.shipping.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ShippingProviderRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Double price;

    private String deliveryTime;
}
