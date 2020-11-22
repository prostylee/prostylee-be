package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequest {

    @NotBlank
    private String fullName;

    private String phoneNumber;

    private Character gender;

    @Email
    private String email;

    private Boolean allowNotification;
}
