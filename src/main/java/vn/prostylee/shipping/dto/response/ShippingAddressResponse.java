package vn.prostylee.shipping.dto.response;

import lombok.Data;

@Data
public class ShippingAddressResponse {

    private Long id;

    private String fullName;

    private String phoneNumber;

    private String address1;

    private String address2;
}
