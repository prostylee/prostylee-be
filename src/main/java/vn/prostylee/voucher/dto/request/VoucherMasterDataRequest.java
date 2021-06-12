package vn.prostylee.voucher.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class VoucherMasterDataRequest {

    @NotBlank
    @Length(max = 64)
    private String group;

    @NotBlank
    @Length(max = 512)
    private String name;

    @NotBlank
    @Length(max = 64)
    private String key;

    @Length(max = 4096)
    private String description;

    private Integer priority;

    private Boolean active;
}
