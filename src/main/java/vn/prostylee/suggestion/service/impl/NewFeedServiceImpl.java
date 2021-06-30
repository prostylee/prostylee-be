package vn.prostylee.suggestion.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.product.converter.ProductConverter;
import vn.prostylee.suggestion.dto.filter.NewFeedFilter;
import vn.prostylee.suggestion.dto.response.NewFeedResponse;
import vn.prostylee.suggestion.service.NewFeedService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewFeedServiceImpl implements NewFeedService {

    private final AuthenticatedProvider authenticatedProvider;

    private final UserFollowerService userFollowerService;

    private final ProductConverter productConverter;


    @Override
    public Page<NewFeedResponse> getNewFeed(NewFeedFilter newFeedFilter) {
        Long userId = this.authenticatedProvider.getUserIdValue();
        UserFollowerFilter userFollowerFilter = UserFollowerFilter.builder().userId(userId).targetType(TargetType.USER.name()).build();
        Page<UserFollowerResponse> userFollowers = this.userFollowerService.findAll(userFollowerFilter);
        return null;
    }
}
