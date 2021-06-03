package vn.prostylee.voucher.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VoucherRequest {

    @NotBlank
    private String fullName;

    private String email;

    private String phoneNumber;

    @NotBlank
    private String address1;

    private String address2;

    private String state;

    private String city;

    private String country;

    private String zipcode;
}
