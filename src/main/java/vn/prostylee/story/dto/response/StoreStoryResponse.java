package vn.prostylee.story.dto.response;

import lombok.Data;
import vn.prostylee.auth.entity.User;
import vn.prostylee.store.entity.Store;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class StoreStoryResponse {

    private Long id;
    private Long targetId;
    private String targetType;
    private Date createdAt;
    private Date deletedAt;
    private Long createdBy;
    private List<String> storyImageUrls;
    private StoreForStoryResponse storeForStoryResponse;
    private Long productId;
}
