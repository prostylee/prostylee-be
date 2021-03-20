package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.filter.MostActiveUserFilter;
import vn.prostylee.useractivity.dto.request.MostActiveUserRequest;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;
import vn.prostylee.useractivity.service.UserActivityService;
import vn.prostylee.useractivity.service.UserFollowerService;
import vn.prostylee.useractivity.service.UserLikeService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private static final int NUMBER_OF_CONDITIONS = 2; // followers and likes

    private final UserService userService;
    private final LocationService locationService;
    private final UserLikeService userLikeService;
    private final UserFollowerService userFollowerService;

    @Override
    public Page<UserActivityResponse> getMostActiveUsers(MostActiveUserFilter filter) {
        MostActiveUserRequest request = MostActiveUserRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.USER.name()))
                .limit(filter.getLimit() / NUMBER_OF_CONDITIONS)
                .fromDate(DateUtils.getLastDaysBefore(filter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build();

        Set<Long> userIds = new LinkedHashSet<>(getUserIdsOfTopBeFollows(request)); // Priority #1
        request.setLimit(filter.getLimit());  // To make sure get enough the number of users requested
        userIds.addAll(getUserIdsOfTopBeLikes(request)); // Priority #2
        if (userIds.size() > filter.getLimit()) {
            userIds = userIds.stream().limit(filter.getLimit()).collect(Collectors.toSet());
        }

        List<UserActivityResponse> responses = getMostActiveUsersByUserIds(new ArrayList<>(userIds));
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
        if (id == null) {
            return null;
        }
        try {
            return locationService.findById(id);
        } catch (ResourceNotFoundException e) {
            log.error("Could not found a location of userId={}", id, e);
        }
        return null;
    }

    private List<Long> getUserIdsOfTopBeFollows(MostActiveUserRequest request) {
        return userFollowerService.getTopBeFollows(request);
    }

    private List<Long> getUserIdsOfTopBeLikes(MostActiveUserRequest request) {
        return userLikeService.getTopBeLikes(request);
    }
}
