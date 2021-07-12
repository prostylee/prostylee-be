package vn.prostylee.comment.dto.response;

import lombok.Data;
import vn.prostylee.auth.entity.User;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.core.constant.TargetType;

import java.util.Date;
import java.util.Set;

@Data
public class CommentResponse {

    private Long id;
    private String content;
    private Long parentId;
    private Long targetId;
    private TargetType targetType;
    private Date deletedAt;
    private Date createdAt;
    private Long createdBy;
    private Set<CommentImage> commentImages;
    private User user;

}
