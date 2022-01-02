package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.UserAddressResponse;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.UserAddress;
import vn.prostylee.auth.service.UserAddressService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.MostActiveUserFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.request.StatusFollowRequest;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;
import vn.prostylee.useractivity.service.UserActivityService;
import vn.prostylee.useractivity.service.UserFollowerService;
import vn.prostylee.useractivity.service.UserMostActiveService;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    public static final int FIRST_INDEX = 0;
    private final UserMostActiveService userMostActiveService;
    private final UserService userService;
    private final LocationService locationService;
    private final UserFollowerService userFollowerService;
    private final UserAddressService userAddressService;

    @Override
    public Page<UserActivityResponse> getMostActiveUsers(MostActiveUserFilter filter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.USER))
                .fromDate(DateUtils.getLastDaysBefore(filter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build()
                .pagingParam(new PagingParam(filter.getPage(), filter.getLimit()));

        List<Long> userIds = userMostActiveService.getTargetIdsByMostActive(request);
        List<UserActivityResponse> responses = getMostActiveUsersByUserIds(userIds);
        return new PageImpl<>(responses);
    }

    private List<UserActivityResponse> getMostActiveUsersByUserIds(List<Long> userIds) {
        return userService.findUsersByIds(userIds)
                .stream()
                .map(this::convertToUserActivityResponse)
                .collect(Collectors.toList());
    }

    private UserActivityResponse convertToUserActivityResponse(UserResponse userResponse) {
        UserActivityResponse userActivityResponse = BeanUtil.copyProperties(userResponse, UserActivityResponse.class);
        userActivityResponse.setLocationResponse(getLocationById(userResponse.getLocationId()));
        userActivityResponse.setFollowStatusOfUserLogin(getFollowStatusOfUserLogin(userResponse.getId()));
        userActivityResponse.setUserAddressResponse(null); // TODO
        return userActivityResponse;
    }

    private LocationResponse getLocationById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(locationService::fetchById)
                .orElse(null);
    }

    public Boolean getFollowStatusOfUserLogin(Long userID) {
        StatusFollowRequest request = StatusFollowRequest.builder()
                .targetIds(Collections.singletonList(userID))
                .targetType(TargetType.USER).build();
        List<Long> result = userFollowerService.loadStatusFollows(request);
        if (result.stream().count() > 0) {
            return true;
        }
        return false;
    }
}
