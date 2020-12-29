package vn.prostylee.comment.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentImageResponse {
    private Long id;
    private Long attachmentId;
    private Long order;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private LocalDateTime createdAt;
}
