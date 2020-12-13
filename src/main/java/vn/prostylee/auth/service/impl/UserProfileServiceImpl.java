package vn.prostylee.auth.service.impl;

import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.core.provider.AuthenticatedProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.service.UserService;

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
                .build();

        return userService.update(userRequest.getId(), userRequest);
    }

    private Long getLoggedInUserId() {
        return authenticatedProvider.getUserId().get();
    }
}
