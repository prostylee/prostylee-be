package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.CheckFollowRequest;
import vn.prostylee.useractivity.dto.request.CheckLikeRequest;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-followers")
public class UserFollowerController {

    private final UserFollowerService service;

    @GetMapping
    public Page<UserFollowerResponse> findAll(UserFollowerFilter filter) {
        return service.findAll(filter);
    }

    @GetMapping("/count")
    public long count(UserFollowerFilter filter) {
        return service.count(filter);
    }

    @PostMapping("/follow")
    public UserFollowerResponse follow(@RequestBody UserFollowerRequest request) {
        return service.follow(request);
    }

    @PutMapping("/unfollow")
    public boolean unfollow(@RequestBody UserFollowerRequest request) {
        return service.unfollow(request);
    }

    @PostMapping("/checkFollow")
    public ResponseEntity<SimpleResponse> checkStatusFollow(@RequestBody CheckFollowRequest checkFollowRequest) {
        List<Long> existIds = service.checkStatusFollow(checkFollowRequest);
        int targetIdListSize = checkFollowRequest.getTargetIds().size();

        if (CollectionUtils.isEmpty(existIds)) {
            return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.NO_CONTENT);
        }else if(existIds.size() < targetIdListSize) {
            return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.PARTIAL_CONTENT);
        }

        return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.OK);
    }

    private SimpleResponse getSimpleResponse(List<Long> existIds) {
        return SimpleResponse.builder().data(existIds).build();
    }
}
