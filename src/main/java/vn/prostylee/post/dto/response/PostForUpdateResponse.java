package vn.prostylee.post.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostForUpdateResponse extends PostResponse{
    private Set<PostImageResponse> postImages;
}
