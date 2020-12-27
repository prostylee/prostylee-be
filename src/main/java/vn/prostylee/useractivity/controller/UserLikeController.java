package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.dto.filter.UserActivityFilter;
import vn.prostylee.useractivity.dto.request.UserActivityRequest;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;
import vn.prostylee.useractivity.service.UserLikeService;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-likes")
public class UserLikeController {
    private final UserLikeService service;

    @GetMapping("/count")
    public Page<UserActivityResponse> findAll(@RequestBody UserActivityRequest request, UserActivityFilter filter) {
        return service.findAll(request,filter);
    }

    @GetMapping("/count")
    public long count(@RequestBody UserActivityRequest request, UserActivityFilter filter) {
        return service.count(request, filter);
    }

    @PostMapping("/like")
    public UserActivityResponse like(@RequestBody UserActivityRequest request) {
        return service.like(request);
    }

    @PostMapping("/unlike/{id}")
    public boolean unlike(@PathVariable Long id) {
        return service.unlike(id);
    }


}
