package vn.prostylee.shipping.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShippingMethodRequest {

    @NotBlank
    private String name;

    private String description;
}
