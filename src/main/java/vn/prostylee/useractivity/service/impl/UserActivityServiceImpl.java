package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.filter.LocationFilter;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.useractivity.dto.filter.UserActivityFilter;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;
import vn.prostylee.useractivity.service.UserActivityService;

@RequiredArgsConstructor
@Service
public class UserActivityServiceImpl implements UserActivityService {

    private final UserService userService;
    private final LocationService locationService;

    @Override
    public Page<UserActivityResponse> getMostActives(UserActivityFilter filter) {
        UserFilter userFilter = BeanUtil.copyProperties(filter, UserFilter.class);
        return userService.findAll(userFilter).map(userResponse -> {
            UserActivityResponse userActivityResponse = BeanUtil.copyProperties(userResponse, UserActivityResponse.class);
            // TODO get location of user
            userActivityResponse.setLocationResponse(locationService.findAll(new LocationFilter()).stream().findFirst().orElse(null));
            return userActivityResponse;
        });
    }
}
