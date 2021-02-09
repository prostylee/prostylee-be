package vn.prostylee.useractivity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.dto.filter.UserActivityFilter;
import vn.prostylee.useractivity.dto.response.UserActivityResponse;
import vn.prostylee.useractivity.service.UserActivityService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-activities")
public class UserActivityController {

    private final UserActivityService service;

    @GetMapping("/most-actives")
    public Page<UserActivityResponse> findAll(UserActivityFilter filter) {
        return service.getMostActives(filter);
    }
}
