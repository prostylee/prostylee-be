package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.validator.UniqueEntity;
import vn.prostylee.core.validator.UniqueIdentifier;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@UniqueEntity(service = UserService.class, fieldNames = {"username"})
public class UserRequest implements UniqueIdentifier<Long> {

    private Long id;

    @NotBlank
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

    private Boolean allowSaleNotification;

    private Boolean allowSocialNotification;

    private Boolean allowOrderStatusNotification;

    private Boolean allowStockNotification;

    private String pushToken;

    @JsonIgnore
    private String sub;

    @JsonIgnore
    private Boolean emailVerified;

    @JsonIgnore
    private Boolean phoneNumberVerified;

}
