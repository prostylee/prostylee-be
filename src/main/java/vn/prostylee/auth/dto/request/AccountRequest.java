package vn.prostylee.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import vn.prostylee.auth.service.AccountService;
import vn.prostylee.core.validator.UniqueEntity;
import vn.prostylee.core.validator.UniqueIdentifier;
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
@JsonIgnoreProperties(
        value = {"password"},
        allowSetters = true
)
@UniqueEntity(service = AccountService.class, serviceQualifier = "accountService", fieldNames = {"username"})
public class AccountRequest implements UniqueIdentifier<Long> {

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
    @JsonIgnore
    private String email;

    private List<String> roles;

    @NotNull
    private Boolean active;

    private Boolean allowNotification;

    private String pushToken;
}
