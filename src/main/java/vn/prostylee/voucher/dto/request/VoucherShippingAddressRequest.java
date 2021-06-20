package vn.prostylee.voucher.dto.request;

import lombok.Data;

@Data
public class VoucherShippingAddressRequest {

    private String state;

    private String city;

    private String country;

    private String zipcode;
}
