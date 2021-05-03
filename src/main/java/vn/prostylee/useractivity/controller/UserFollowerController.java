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
import vn.prostylee.useractivity.dto.filter.UserFollowerPageable;
import vn.prostylee.useractivity.dto.filter.UserFollowingFilter;
import vn.prostylee.useractivity.dto.request.StatusFollowRequest;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

import javax.validation.Valid;
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
    public SimpleResponse follow(@Valid  @RequestBody UserFollowerRequest request) {
        return SimpleResponse.builder().data(service.follow(request)).build();
    }

    @PutMapping("/unfollow")
    public SimpleResponse unfollow(@Valid @RequestBody UserFollowerRequest request) {
        return SimpleResponse.builder().data(service.unfollow(request)).build();
    }

    @GetMapping("/me/followers")
    public Page<Long> getFollowersByMe(UserFollowerPageable userFollowerPageable) {
        return service.getFollowersByMe(userFollowerPageable);
    }

    @GetMapping("/{id}/followers")
    public Page<Long> getFollowersByUserId(@PathVariable Long id, UserFollowerPageable userFollowerPageable) {
        return service.getFollowersByUserId(id, userFollowerPageable);
    }

    @GetMapping("/me/followings")
    public Page<Long> getFollowingsByMe(@Valid UserFollowingFilter filter) {
        return service.getFollowingsByMe(filter);
    }

    @GetMapping("/{id}/followings")
    public Page<Long> getFollowingsByUserId(@PathVariable Long id, @Valid UserFollowingFilter filter) {
        return service.getFollowingsByUserId(id, filter);
    }

    @PostMapping("/load-status-follow")
    public ResponseEntity<SimpleResponse> loadStatusFollow(@RequestBody StatusFollowRequest statusFollowRequest) {
        List<Long> existIds = service.loadStatusFollows(statusFollowRequest);
        int targetIdListSize = statusFollowRequest.getTargetIds().size();

        if (CollectionUtils.isEmpty(existIds)) {
            return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.NO_CONTENT);
        } else if (existIds.size() < targetIdListSize) {
            return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.PARTIAL_CONTENT);
        }

        return new ResponseEntity<>(getSimpleResponse(existIds), HttpStatus.OK);
    }

    private SimpleResponse getSimpleResponse(List<Long> existIds) {
        return SimpleResponse.builder().data(existIds).build();
    }
}
