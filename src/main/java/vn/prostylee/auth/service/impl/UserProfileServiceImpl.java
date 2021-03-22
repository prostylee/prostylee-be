package vn.prostylee.auth.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.provider.AuthenticatedProvider;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final AuthenticatedProvider authenticatedProvider;
    private final UserService userService;

    @Override
    public UserResponse getProfile() {
        Long userId = getLoggedInUserId();
        return userService.findById(userId);
    }

    @Override
    public UserResponse updateProfile(UserProfileRequest request) {
        Long userId = getLoggedInUserId();
        UserResponse userResponse = userService.findById(userId);
        UserRequest userRequest = UserRequest.builder()
                .id(userId)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .gender(request.getGender())
                .roles(new ArrayList<>(userResponse.getRoles()))
                .allowNotification(request.getAllowNotification())
                .allowSaleNotification(request.getAllowSaleNotification())
                .allowOrderStatusNotification(request.getAllowOrderStatusNotification())
                .allowSocialNotification(request.getAllowSocialNotification())
                .allowStockNotification(request.getAllowStockNotification())
                .build();

        return userService.update(userRequest.getId(), userRequest);
    }

    @Override
    public UserResponse getProfileBy(Long id) {
        return userService.findById(id);
    }

    private Long getLoggedInUserId() {
        return authenticatedProvider.getUserIdValue();
    }
}
