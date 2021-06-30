package vn.prostylee.suggestion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.post.dto.response.PostResponse;
import vn.prostylee.product.dto.response.ProductOwnerResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.useractivity.constant.TargetType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewFeedResponse {

    private ProductOwnerResponse productOwner;

    private Boolean followStatusOfUserLogin;

    private TargetType targetType;

    private ProductResponse product;

    private PostResponse post;
}
