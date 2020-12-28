package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.ChangePasswordRequest;
import vn.prostylee.auth.dto.request.ForgotPasswordRequest;

public interface UserPasswordService {

    boolean forgotPassword(ForgotPasswordRequest request);

    boolean changePassword(ChangePasswordRequest request);
}
