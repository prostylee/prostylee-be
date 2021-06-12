package vn.prostylee.voucher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.response.AuditResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherMasterDataResponse extends AuditResponse {

    private Long id;

    private String group;

    private String name;

    private String key;

    private String description;

    private Integer priority;

    private Boolean active;
}
