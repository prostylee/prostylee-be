package vn.prostylee.story.dto.response;

import lombok.Data;
import vn.prostylee.auth.entity.User;
import vn.prostylee.product.entity.Product;
import vn.prostylee.story.entity.StoryImage;

import java.util.Date;
import java.util.Set;

@Data
public class StoryResponse {

    private Long id;
    private Long targetId;
    private String targetType;
    private Date createdAt;
    private Date deletedAt;
    private Long createdBy;
    private Set<StoryImage> storyImages;
    private User user;
    private Long productId;
}
