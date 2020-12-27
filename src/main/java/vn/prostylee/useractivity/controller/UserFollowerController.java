package vn.prostylee.useractivity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.comment.dto.filter.CommentFilter;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.dto.filter.UserFollowerFilter;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;
import vn.prostylee.useractivity.entity.UserFollower;
import vn.prostylee.useractivity.service.UserFollowerService;

import javax.websocket.server.PathParam;

@RestController
@Slf4j
@RequestMapping(value = ApiVersion.API_V1 + "/user-followers")
public class UserFollowerController {
    private UserFollowerService service;

    @GetMapping("/count")
    public Page<UserFollowerResponse> findAll(@RequestBody UserFollowerRequest request, UserFollowerFilter filter) {
        return service.findAll(request,filter);
    }

    @GetMapping("/count")
    public long count(@RequestBody UserFollowerRequest request, UserFollowerFilter filter) {
        return service.count(filter, request);
    }

    @PostMapping("/follow")
    public UserFollowerResponse follow(@RequestBody UserFollowerRequest request) {
        return service.follow(request);
    }

    @PostMapping("/unfollow/{id}")
    public boolean unfollow(@PathVariable Long id) {
        return service.unfollow(id);
    }

}
