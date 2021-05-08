package vn.prostylee.post.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostImageResponse implements Serializable {
    private Long id;
    private Integer order;
    private Long attachmentId;
    private String url;
}
