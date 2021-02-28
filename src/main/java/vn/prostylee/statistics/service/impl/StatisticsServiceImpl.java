package vn.prostylee.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.statistics.constant.StatisticsType;
import vn.prostylee.statistics.dto.response.UserStatisticsResponse;
import vn.prostylee.statistics.service.StatisticsService;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.service.UserFollowerService;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AuthenticatedProvider authenticatedProvider;
    private final UserFollowerService userFollowerService;
    private final ProductService productService;
    //TODO private final UserPostService userPostService;

    @Override
    public UserStatisticsResponse getUserActivities() {
        Long userLoginId = authenticatedProvider.getUserIdValue();

        UserFollowerFilter followerFilter = UserFollowerFilter.builder()
                .targetId(userLoginId)
                .targetType(StatisticsType.USER.getType())
                .build();

        UserFollowerFilter followingFilter = UserFollowerFilter.builder()
                .targetType(StatisticsType.USER.getType())
                .userId(userLoginId)
                .build();

        long followers = userFollowerService.count(followerFilter);
        long following = userFollowerService.count(followingFilter);
        long productPosts = productService.countTotalProductByUser(userLoginId);

        return UserStatisticsResponse.builder()
                .productPosts(productPosts)
                .posts(0)
                .followers(followers)
                .followings(following)
                .build();
    }
}
