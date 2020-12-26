package vn.prostylee.useractivity.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.useractivity.service.UserFollowerService;

@RestController
@Slf4j
@RequestMapping(value = ApiVersion.API_V1 + "/user-followers")
public class UserFollowerController {
    private UserFollowerService service;

}
