package vn.prostylee.statistics.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.post.service.PostService;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.statistics.dto.response.StoreStatisticsResponse;
import vn.prostylee.statistics.dto.response.UserStatisticsResponse;
import vn.prostylee.statistics.service.StatisticsService;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.filter.UserRatingFilter;
import vn.prostylee.useractivity.service.UserFollowerService;
import vn.prostylee.useractivity.service.UserRatingService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final AuthenticatedProvider authenticatedProvider;
    private final UserFollowerService userFollowerService;
    private final ProductService productService;
    private final PostService postService;
    private final UserRatingService userRatingService;

    @Override
    public UserStatisticsResponse getUserActivities() {
        Long userLoginId = authenticatedProvider.getUserIdValue();
        return getUserStatisticsResponse(userLoginId);
    }

    @Override
    public UserStatisticsResponse getUserActivitiesByUserId(Long userId) {
        return getUserStatisticsResponse(userId);
    }

    @Override
    public StoreStatisticsResponse getStoreStatisticByStoreId(Long storeId) {
        return getStoreStatisticsResponse(storeId);
    }

    private StoreStatisticsResponse getStoreStatisticsResponse(Long storeId) {
        UserFollowerFilter followerFilter = UserFollowerFilter.builder()
                .targetId(storeId)
                .targetType(TargetType.STORE)
                .build();
        long followers = userFollowerService.count(followerFilter);
        long productPosts = productService.countTotalProductByStoreId(storeId);
        long posts = postService.countTotalPostsByStoreId(storeId);

        UserRatingFilter filter = new UserRatingFilter();
        filter.setTargetId(storeId);
        filter.setTargetType(TargetType.STORE);
        Double calculatorRating = userRatingService.average(filter);

        return StoreStatisticsResponse.builder()
                .numberOfProductPosts(productPosts)
                .numberOfPosts(posts)
                .numberOfFollowers(followers)
                .rating(calculatorRating)
                .build();
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
