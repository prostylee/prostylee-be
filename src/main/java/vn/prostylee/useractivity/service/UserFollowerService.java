package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;

public interface UserFollowerService {

    long count(BaseFilter baseFilter, UserFollowerRequest request);

    Page<UserFollowerResponse> findAll(BaseFilter baseFilter, UserFollowerRequest request);

    UserFollowerResponse follow(Long id, UserFollowerRequest request);

    boolean unfollow(Long id);
}
