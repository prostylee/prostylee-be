package vn.prostylee.product.dto.response;

import lombok.Data;
import vn.prostylee.core.entity.AuditEntity;

@Data
public class BrandResponse extends AuditEntity {
    private Long id;
    private String description;
    private String name;
}
