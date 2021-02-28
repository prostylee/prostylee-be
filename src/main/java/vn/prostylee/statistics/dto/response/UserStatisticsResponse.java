package vn.prostylee.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticsResponse {
    private long followers;
    private long followings;
    private long posts;
    private long productPosts;
}
