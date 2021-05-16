package vn.prostylee.shipping.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShippingAddressResponse implements Serializable {

    private Long id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address1;

    private String address2;

    private String state;

    private String city;

    private String country;

    private String zipcode;
}
