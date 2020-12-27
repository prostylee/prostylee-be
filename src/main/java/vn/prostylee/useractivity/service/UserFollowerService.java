package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserActivityFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;

public interface UserFollowerService {

    long count(UserActivityRequest request, UserActivityFilter filter);

    Page<UserActivityResponse> findAll(UserActivityRequest request, UserActivityFilter filter);

    UserActivityResponse follow(UserActivityRequest request);

    boolean unfollow(Long id);
}
