package vn.prostylee.post.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostImageRequest {
    private String name;
    private String path;

}
