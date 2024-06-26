package vn.prostylee.product.dto.response;

import lombok.*;
import vn.prostylee.post.dto.response.PostStatisticResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewFeedResponse implements Serializable {

    private String type;

    private Long ownerId;

    private Long id;

    private String content;

    private Long storeAdsId;

    private StoreResponseLite storeAdsResponseLite;

    private Double price;

    private Double priceSale;

    private List<String> imageUrls;

    private Boolean isAdvertising;

    private Boolean likeStatusOfUserLogin;

    private Boolean saveStatusOfUserLogin;

    private Boolean followStatusOfUserLogin;

    private ProductStatisticResponse productStatisticResponse;

    private PostStatisticResponse postStatisticResponse;

    private ProductOwnerResponse newFeedOwnerResponse;
}
