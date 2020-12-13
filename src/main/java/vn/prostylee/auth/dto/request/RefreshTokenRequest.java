package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotBlank
    @Size(max = 4096)
    private String refreshToken;
}
