package vn.prostylee.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ForgotPasswordRequest {

    @Email
    @NotNull
    private String email;
}
