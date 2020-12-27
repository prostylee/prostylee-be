package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;

public interface UserLikeService {
    long count(UserActivityRequest request, UserLikeFilter baseFilter);

    Page<UserLikeResponse> findAll(UserActivityRequest request, UserLikeFilter filter);

    UserLikeResponse like(UserActivityRequest request);

    boolean unlike(Long id);
}
