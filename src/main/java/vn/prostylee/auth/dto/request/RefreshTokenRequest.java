package vn.prostylee.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RefreshTokenRequest {

    @NotBlank
    @Size(max = 4096)
    private String refreshToken;
}
