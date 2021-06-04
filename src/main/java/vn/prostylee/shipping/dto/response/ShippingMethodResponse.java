package vn.prostylee.shipping.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.response.AuditResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShippingMethodResponse extends AuditResponse {
    private Long id;
    private String name;
    private String description;
}
