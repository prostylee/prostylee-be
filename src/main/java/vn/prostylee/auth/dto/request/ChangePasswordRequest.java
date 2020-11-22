package vn.prostylee.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String newPassword;
}
