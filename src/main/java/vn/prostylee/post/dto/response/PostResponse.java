package vn.prostylee.post.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.entity.AuditEntity;
import vn.prostylee.store.dto.response.StoreResponseLite;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostResponse extends AuditEntity {
    private Long id;
    private String description;
    private StoreResponseLite storeResponseLite;
}
