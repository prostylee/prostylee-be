package vn.prostylee.statistics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserStatisticsResponse implements Serializable {
    private long followers;
    private long followings;
    private long posts;
    private long productPosts;
}
