package vn.prostylee.shipping.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShippingAddressResponse implements Serializable {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String address1;

    private String address2;
}
