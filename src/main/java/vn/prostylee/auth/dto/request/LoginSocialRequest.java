package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import vn.prostylee.auth.constant.SocialProviderType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginSocialRequest {
    @NotBlank
    private String appClientId;

    @NotBlank
    @Length(max = 128)
    private String appClientSecret;

    @NotBlank
    private String idToken;

    @NotBlank
    private SocialProviderType  providerType;
}
