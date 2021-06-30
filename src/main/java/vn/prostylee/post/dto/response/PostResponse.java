package vn.prostylee.post.dto.response;

import lombok.Data;
import vn.prostylee.product.dto.response.ProductStatisticResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.story.dto.response.UserResponseLite;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PostResponse implements Serializable {
    private Long id;
    private String description;
    private StoreResponseLite storeResponseLite;
    private UserResponseLite userResponseLite;
    private List<String> imageUrls;
    private ProductStatisticResponse productStatistic;
    private Long updatedBy;
    private Date updatedAt;
    private Long createdBy;
    private Date createdAt;
}