package vn.prostylee.shipping.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShippingProviderResponse implements Serializable {
    private String name;
    private String description;
    private Double price;
}
