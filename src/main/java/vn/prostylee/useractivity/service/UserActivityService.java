package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.useractivity.dto.filter.MostActiveUserFilter;
import vn.prostylee.useractivity.dto.filter.UserActivityFilter;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;

public interface UserActivityService {

    Page<UserActivityResponse> getMostActiveUsers(MostActiveUserFilter filter);
    Boolean getFollowStatusOfUserLogin(Long userId);
}
