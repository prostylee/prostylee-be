package vn.prostylee.story.dto.response;

import lombok.Data;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.store.entity.Store;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class StoreStoryResponse implements Serializable {

    private Long id;
    private Long targetId;
    private TargetType targetType;
    private Date createdAt;
    private Date deletedAt;
    private Long createdBy;
    private List<String> storySmallImageUrls;
    private List<String> storyLargeImageUrls;
    private StoreForStoryResponse storeForStoryResponse;
    private Long productId;
    private ProductResponseLite productResponseLite;
}
