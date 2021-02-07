package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.response.UserResponse;

public interface UserProfileService {

    UserResponse getProfile();

    UserResponse updateProfile(UserProfileRequest request);

    UserResponse getProfileBy(Long Id);
}
