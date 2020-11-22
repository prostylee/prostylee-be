package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    private String appClientId;

    @NotBlank
    @Length(max = 128)
    private String appClientSecret;

    @NotBlank
    @Email
    @Size(min = 4, max = 256)
    private String username;

    @NotBlank
    @Size(min = 4, max = 128)
    private String password;

}