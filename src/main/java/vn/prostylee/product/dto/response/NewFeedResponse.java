package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.post.dto.response.PostStatisticResponse;

import java.io.Serializable;
import java.util.Date;
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

    private Double price;

    private Double priceSale;

    private List<String> imageUrls;

    private Boolean isAdvertising;

    private Boolean likeStatusOfUserLogin;

    private Boolean saveStatusOfUserLogin;

    private ProductStatisticResponse productStatisticResponse;

    private PostStatisticResponse postStatisticResponse;

    private ProductOwnerResponse newFeedOwnerResponse;
}
