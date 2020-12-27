package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;

public interface UserFollowerService {

    long count(UserFollowerFilter filter);

    Page<UserFollowerResponse> findAll(UserFollowerFilter filter);

    UserFollowerResponse follow(UserFollowerRequest request);

    boolean unfollow(UserFollowerRequest request);
}
