package vn.prostylee.post.dto.response;

import lombok.Data;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.story.dto.response.UserResponseLite;

import java.io.Serializable;
import java.util.Date;

@Data
public class PostResponse implements Serializable {
    private Long id;
    private String description;
    private StoreResponseLite storeResponseLite;
    private StoreResponseLite storeOwnerResponseLite;
    private UserResponseLite userResponseLite;
    private Long updatedBy;
    private Date updatedAt;
    private Long createdBy;
    private Date createdAt;
}
