package vn.prostylee.post.dto.response;

import lombok.Data;

@Data
public class PostImageResponse {
    private Long id;
    private Integer order;
    private Long attachmentId;
}
