package vn.prostylee.comment.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentImageResponse {
    private Long id;
    private Long attachmentId;
    private Long order;
    private Long updatedBy;
    private Date updatedAt;
    private Long createdBy;
    private Date createdAt;
}
