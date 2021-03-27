package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.MostActiveUserFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;
import vn.prostylee.useractivity.service.UserActivityService;
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

    private final UserMostActiveService userMostActiveService;
    private final UserService userService;
    private final LocationService locationService;

    @Override
    public Page<UserActivityResponse> getMostActiveUsers(MostActiveUserFilter filter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.USER.name()))
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
        return userActivityResponse;
    }

    private LocationResponse getLocationById(Long id) {
        return Optional.ofNullable(id)
                .flatMap(locationService::fetchById)
                .orElse(null);
    }
}
