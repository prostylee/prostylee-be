package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserFollowerService;
import vn.prostylee.useractivity.service.UserLikeService;
import vn.prostylee.useractivity.service.UserMostActiveService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserMostActiveServiceImpl implements UserMostActiveService {

    private static final int NUMBER_OF_CONDITIONS = 2; // followers and likes

    private final UserLikeService userLikeService;
    private final UserFollowerService userFollowerService;

    @Override
    public List<Long> getTargetIdsByMostActive(MostActiveRequest request) {
        final int originalLimit = request.getLimit();
        request.setLimit(originalLimit / NUMBER_OF_CONDITIONS);
        Set<Long> userIds = new LinkedHashSet<>(getUserIdsOfTopBeFollows(request)); // Priority #1

        request.setLimit(originalLimit);  // To make sure get enough the number of users requested
        userIds.addAll(getUserIdsOfTopBeLikes(request)); // Priority #2

        if (userIds.size() > originalLimit) { // Only get a number of users equals with the request limit
            userIds = userIds.stream().limit(originalLimit).collect(Collectors.toSet());
        }
        return new ArrayList<>(userIds);
    }

    private List<Long> getUserIdsOfTopBeFollows(MostActiveRequest request) {
        return userFollowerService.getTopBeFollows(request);
    }

    private List<Long> getUserIdsOfTopBeLikes(MostActiveRequest request) {
        return userLikeService.getTopBeLikes(request);
    }
}
