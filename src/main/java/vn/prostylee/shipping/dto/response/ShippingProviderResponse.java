package vn.prostylee.shipping.dto.response;

import lombok.Data;

@Data
public class ShippingProviderResponse {
    private String name;
    private String description;
    private Double price;
}
