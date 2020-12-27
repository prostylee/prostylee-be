package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserActivityFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;

public interface UserLikeService {
    long count(UserActivityRequest request, UserActivityFilter baseFilter);

    Page<UserActivityResponse> findAll(UserActivityRequest request, UserActivityFilter filter);

    UserActivityResponse like(UserActivityRequest request);

    boolean unlike(Long id);
}
