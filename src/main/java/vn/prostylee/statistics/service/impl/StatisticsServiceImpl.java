package vn.prostylee.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.post.service.PostService;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.statistics.dto.response.UserStatisticsResponse;
import vn.prostylee.statistics.service.StatisticsService;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AuthenticatedProvider authenticatedProvider;
    private final UserFollowerService userFollowerService;
    private final ProductService productService;
    private final PostService postService;

    @Override
    public UserStatisticsResponse getUserActivities() {
        Long userLoginId = authenticatedProvider.getUserIdValue();
        return getUserStatisticsResponse(userLoginId);
    }

    @Override
    public UserStatisticsResponse getUserActivitiesByUserId(Long userId) {
        return getUserStatisticsResponse(userId);
    }

    private UserStatisticsResponse getUserStatisticsResponse(Long userLoginId) {
        UserFollowerFilter followerFilter = UserFollowerFilter.builder()
                .targetId(userLoginId)
                .targetTypes(Arrays.asList(TargetType.USER, TargetType.STORE))
                .build();

        UserFollowerFilter followingFilter = UserFollowerFilter.builder()
                .targetTypes(Arrays.asList(TargetType.USER, TargetType.STORE))
                .userId(userLoginId)
                .build();

        long followers = userFollowerService.count(followerFilter);
        long following = userFollowerService.count(followingFilter);
        long numberOfProducts = productService.countTotalProductByUser(userLoginId);
        long numberOfPosts = postService.countTotalPostByUser(userLoginId);

        return UserStatisticsResponse.builder()
                .productPosts(numberOfProducts)
                .posts(numberOfPosts)
                .followers(followers)
                .followings(following)
                .build();
    }
}
