package vn.prostylee.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.prostylee.core.validator.UniqueEntity;
import vn.prostylee.core.validator.UniqueIdentifier;
import vn.prostylee.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@UniqueEntity(service = UserService.class, fieldNames = {"username"})
public class UserRequest implements UniqueIdentifier<Long> {

    private Long id;

    @NotBlank
    @Email
    private String username;

    private String password;

    @NotBlank
    private String fullName;

    private String phoneNumber;

    private Character gender;

    @Email
    private String email;

    private List<String> roles;

    private Boolean active;

    private Boolean allowNotification;

    private String pushToken;
}
