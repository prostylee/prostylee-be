package vn.prostylee.useractivity.dto.response;

import lombok.Data;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.product.dto.response.ProductResponse;

import java.util.Date;
@Data
public class UserLikeResponse {

    private Long id;

    private Date createdAt;

    private Long createdBy;

    private Long targetId;

    private ProductResponse product;

    private PostResponse post;

    private String targetType;

    private Long customFieldId1;

    private Long customFieldId2;

    private Long customFieldId3;
}
