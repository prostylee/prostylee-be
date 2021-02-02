package vn.prostylee.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String newPassword;
}
