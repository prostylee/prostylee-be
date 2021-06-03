package vn.prostylee.voucher.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class VoucherResponse implements Serializable {

    private Long id;

    private String name;

    private String key;

    private String logo;

    private String voucherOwner;

    private String expiryDate;
}
