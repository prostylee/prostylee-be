package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.UserLikeRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;

public interface UserLikeService {
    long count(UserLikeFilter baseFilter);

    Page<UserLikeResponse> findAll(UserLikeFilter filter);

    UserLikeResponse like(UserLikeRequest request);

    boolean unlike(UserLikeRequest request);
}
