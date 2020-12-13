package vn.prostylee.comment.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CommentRequest {

    private List<Long> attachmentId;

    private String content;

    private Long parentId;

    private Long targetId;

    private String targetType;
}
