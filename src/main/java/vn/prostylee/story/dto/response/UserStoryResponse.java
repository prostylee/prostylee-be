package vn.prostylee.story.dto.response;

import lombok.Data;
import vn.prostylee.core.constant.TargetType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserStoryResponse implements Serializable {

    private Long id;
    private Long targetId;
    private TargetType targetType;
    private Date createdAt;
    private Date deletedAt;
    private Long createdBy;
    private List<String> storySmallImageUrls;
    private List<String> storyLargeImageUrls;
    private UserResponseLite userForStoryResponse;
    private Long storeId;
    private StoreForStoryResponse storeResponseLite;
}
