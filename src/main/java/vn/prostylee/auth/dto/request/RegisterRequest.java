package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.validator.Gender;
import vn.prostylee.core.validator.Unique;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 1, max = 128)
    private String fullName;

    @Size(min = 1, max = 20)
    private String phoneNumber;

    @Unique(service = UserService.class)
    @NotBlank
    @Size(min = 4, max = 256)
    private String username;

    @ToStringExclude
    @NotBlank
    @Size(min = 4, max = 128)
    private String password;

    @Email
    private String email;

    @Gender
    private Character gender;

    private Boolean active;

}