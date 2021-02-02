package vn.prostylee.auth.service;

import vn.prostylee.auth.dto.request.ChangePasswordRequest;
import vn.prostylee.auth.dto.request.ForgotPasswordRequest;
import vn.prostylee.auth.dto.request.OtpVerificationRequest;

public interface UserPasswordService {

    boolean forgotPassword(ForgotPasswordRequest request);

    boolean verifyOtp(OtpVerificationRequest request);

    boolean changePassword(ChangePasswordRequest request);
}
