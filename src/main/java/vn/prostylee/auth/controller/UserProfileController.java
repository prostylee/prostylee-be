package vn.prostylee.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.core.constant.ApiVersion;

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

    @GetMapping("/{id}")
    public UserResponse getProfileById(@PathVariable Long id) {
        return userProfileService.getProfileBy(id);
    }

    @PutMapping
    public UserResponse updateProfile(@Valid @RequestBody UserProfileRequest request) {
        return userProfileService.updateProfile(request);
    }

    @GetMapping("/{sub}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable String sub) {
        return userProfileService.getAvatar(sub);
    }
}
