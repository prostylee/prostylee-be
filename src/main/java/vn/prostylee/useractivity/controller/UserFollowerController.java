package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

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

}
