package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.service.UserFollowerService;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-followers")
public class UserFollowerController {

    private final UserFollowerService service;

    @GetMapping("/count")
    public Page<UserFollowerResponse> findAll(@RequestBody UserActivityRequest request, UserFollowerFilter filter) {
        return service.findAll(request,filter);
    }

    @GetMapping("/count")
    public long count(@RequestBody UserActivityRequest request, UserFollowerFilter filter) {
        return service.count(request, filter);
    }

    @PostMapping("/follow")
    public UserFollowerResponse follow(@RequestBody UserActivityRequest request) {
        return service.follow(request);
    }

    @PostMapping("/unfollow/{id}")
    public boolean unfollow(@PathVariable Long id) {
        return service.unfollow(id);
    }

}
