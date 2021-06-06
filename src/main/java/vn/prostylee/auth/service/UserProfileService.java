package vn.prostylee.auth.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.response.UserResponse;

public interface UserProfileService {

    UserResponse getProfile();

    UserResponse updateProfile(UserProfileRequest request);

    UserResponse getProfileBy(Long id);

    ResponseEntity<Resource> getAvatar(String sub);

}
