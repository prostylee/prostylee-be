package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.validator.UniqueEntity;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLinkAccountRequest {

    private User user;

    @NotBlank
    @Length(max = 45)
    private String providerId;

    @NotBlank
    @Length(max = 45)
    private String providerName;
}
