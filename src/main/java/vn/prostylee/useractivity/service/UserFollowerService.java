package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.MostActiveUserRequest;
import vn.prostylee.useractivity.dto.request.StatusFollowRequest;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;

import java.util.List;

public interface UserFollowerService {

    long count(UserFollowerFilter filter);

    Page<UserFollowerResponse> findAll(UserFollowerFilter filter);

    boolean follow(UserFollowerRequest request);

    boolean unfollow(UserFollowerRequest request);

    List<Long> loadStatusFollows(StatusFollowRequest checkFollowRequest);

    List<Long> getTopBeFollows(MostActiveUserRequest request);
}
