package vn.prostylee.shipping.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShippingMethodResponse implements Serializable {
    private String name;
    private String description;
}
