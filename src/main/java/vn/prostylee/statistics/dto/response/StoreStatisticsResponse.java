package vn.prostylee.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class StoreStatisticsResponse implements Serializable {
    private Long numberOfFollowers;
    private Long numberOfPosts;
    private Long numberOfProductPosts;
    private Double rating;
}
