package vn.prostylee.auth.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class OtpVerificationRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(max = 64)
    private String otp;
}
