package vn.prostylee.comment.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CommentRequest {

    private List<Long> attachmentIds;

    private String content;

    @NotNull
    private Long parentId;

    @NotNull
    private Long targetId;

    private String targetType;
}
