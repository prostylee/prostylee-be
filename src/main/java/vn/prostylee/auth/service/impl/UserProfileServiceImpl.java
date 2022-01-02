package vn.prostylee.auth.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.service.UserProfileService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.EncrytedPasswordUtils;
import vn.prostylee.core.utils.MimeTypeUtil;
import vn.prostylee.useractivity.service.UserActivityService;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final AuthenticatedProvider authenticatedProvider;
    private final UserService userService;
    private final UserActivityService userActivityService;

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
                .bio(request.getBio())
                .date(request.getDate())
                .month(request.getMonth())
                .year(request.getYear())
                .avatarImageInfo(request.getAvatarImageInfo())
                .build();

        return userService.update(userRequest.getId(), userRequest);
    }

    @Override
    public UserResponse getProfileBy(Long id) {
        UserResponse userResponse = userService.findById(id);
        Boolean isFollowByLoggedInUser = userActivityService.getFollowStatusOfUserLogin(id);
        userResponse.setFollowedByLoggedInUser(isFollowByLoggedInUser);
        return userResponse;
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Resource> getAvatar(String sub) {
        Resource resource;
        String avatarUrl = userService.findBySub(sub).map(User::getAvatar).orElse(null);
        if (StringUtils.isBlank(avatarUrl)) {
            avatarUrl = "images/user-avatar.png";
            resource = new ClassPathResource(avatarUrl);
        } else {
            resource = new UrlResource(avatarUrl);
        }
        String extension = FilenameUtils.getExtension(avatarUrl);
        String contentType = MimeTypeUtil.getMimeType(extension);
        String fileName = "avatar-" + System.currentTimeMillis() + "." + extension;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    private Long getLoggedInUserId() {
        return authenticatedProvider.getUserIdValue();
    }
}
