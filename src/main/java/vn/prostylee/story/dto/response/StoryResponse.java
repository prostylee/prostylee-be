package vn.prostylee.story.dto.response;

import lombok.Data;
import vn.prostylee.auth.entity.User;
import vn.prostylee.product.entity.Product;

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
    private Set<StoryImageResponse> storyImages;
    private User user;
    private Product product;
}
