package vn.prostylee.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ForgotPasswordRequest {

    @NotNull
    private String email;

    private String language;
}
