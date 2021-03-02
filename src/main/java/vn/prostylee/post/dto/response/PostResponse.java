package vn.prostylee.post.dto.response;

import lombok.Data;
import vn.prostylee.core.entity.AuditEntity;
import vn.prostylee.store.dto.response.StoreResponseLite;

import java.util.List;
import java.util.Set;

@Data
public class PostResponse extends AuditEntity {
    private Long id;
    private String description;
    private StoreResponseLite storeResponseLite;
    private List<String> imageUrls;
    private Set<PostImageResponse> postImageResponses;
}
