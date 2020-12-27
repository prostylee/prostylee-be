package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;

public interface UserFollowerService {

    long count(UserActivityRequest request, UserFollowerFilter filter);

    Page<UserFollowerResponse> findAll(UserActivityRequest request, UserFollowerFilter filter);

    UserFollowerResponse follow(UserActivityRequest request);

    boolean unfollow(Long id);
}
