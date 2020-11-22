package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.UserProfileRequest;
import vn.prostylee.auth.dto.response.AccountResponse;

public interface UserProfileService {

    AccountResponse getProfile();

    AccountResponse updateProfile(UserProfileRequest request);
}
