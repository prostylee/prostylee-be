package vn.prostylee.auth.controller;

import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.auth.service.UserProfileService;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public UserResponse getProfile() {
        return userProfileService.getProfile();
    }

    @PutMapping
    public UserResponse updateProfile(@Valid @RequestBody UserProfileRequest request) {
        return userProfileService.updateProfile(request);
    }

}
