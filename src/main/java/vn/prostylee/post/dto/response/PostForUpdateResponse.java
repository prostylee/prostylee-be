package vn.prostylee.post.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostForUpdateResponse extends PostResponse implements Serializable {
    private Set<PostImageResponse> postImages;
}
