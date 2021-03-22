package vn.prostylee.story.dto.response;

import lombok.Data;
import vn.prostylee.store.dto.response.StoreResponseLite;

import java.util.Date;
import java.util.List;

@Data
public class UserStoryResponse {

    private Long id;
    private Long targetId;
    private String targetType;
    private Date createdAt;
    private Date deletedAt;
    private Long createdBy;
    private List<String> storySmallImageUrls;
    private List<String> storyLargeImageUrls;
    private UserResponseLite userForStoryResponse;
    private Long storeId;
    private StoreResponseLite storeResponseLite;
}
