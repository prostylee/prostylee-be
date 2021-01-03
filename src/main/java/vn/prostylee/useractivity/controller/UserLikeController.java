package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.UserLikeRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;
import vn.prostylee.useractivity.service.UserLikeService;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-likes")
public class UserLikeController {

    private final UserLikeService service;

    @GetMapping
    public Page<UserLikeResponse> findAll(UserLikeFilter filter) {
        return service.findAll(filter);
    }

    @GetMapping("/count")
    public long count(UserLikeFilter filter) {
        return service.count(filter);
    }

    @PostMapping("/like")
    public UserLikeResponse like(@RequestBody UserLikeRequest request) {
        return service.like(request);
    }

    @PutMapping("/unlike")
    public boolean unlike(@RequestBody UserLikeRequest request) {
        return service.unlike(request);
    }


}
