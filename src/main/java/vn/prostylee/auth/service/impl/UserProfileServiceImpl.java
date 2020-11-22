package vn.prostylee.auth.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.request.AccountRequest;
import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.response.AccountResponse;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.auth.service.AccountService;
import vn.prostylee.auth.service.UserProfileService;

import java.util.ArrayList;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final AuthenticatedProvider authenticatedProvider;
    private final AccountService accountService;

    @Override
    public AccountResponse getProfile() {
        Long userId = getLoggedInUserId();
        return accountService.findById(userId);
    }

    @Override
    public AccountResponse updateProfile(UserProfileRequest request) {
        Long userId = getLoggedInUserId();
        AccountResponse accountResponse = accountService.findById(userId);
        AccountRequest accountRequest = AccountRequest.builder()
                .id(userId)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .gender(request.getGender())
                .roles(new ArrayList<>(accountResponse.getRoles()))
                .allowNotification(request.getAllowNotification())
                .build();

        return accountService.update(accountRequest.getId(), accountRequest);
    }

    private Long getLoggedInUserId() {
        return authenticatedProvider.getUserId().get();
    }
}
