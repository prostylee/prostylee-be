package vn.prostylee.story.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class StoryImageResponse {
    private Long id;
    private Long attachmentId;
    private Long order;
    private Long updatedBy;
    private Date updatedAt;
    private Long createdBy;
    private Date createdAt;
}
