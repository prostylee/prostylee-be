package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;

public interface UserFollowerService {

    long count(BaseFilter baseFilter, UserFollowerRequest request);

    Page<UserFollowerResponse> findAll(UserFollowerRequest request, UserFollowerFilter filter);

    UserFollowerResponse follow(UserFollowerRequest request);

    boolean unfollow(Long id);
}
