package vn.prostylee.story.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserStoryResponse {

    private Long id;
    private Long targetId;
    private String targetType;
    private Date createdAt;
    private Date deletedAt;
    private Long createdBy;
    private List<String> storyImageUrls;
    private UserForStoryResponse userForStoryResponse;
    private Long productId;
}
