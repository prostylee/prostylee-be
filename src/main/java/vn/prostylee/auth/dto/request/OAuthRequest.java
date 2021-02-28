package vn.prostylee.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthRequest {

    @NotBlank
    private String username;

    private String password;

    private String userStatus;

    private Boolean enabled;

    private List<String> groups;

    private long createdAt;

    private long updatedAt;

    @Valid
    @NotNull
    private OAuthUserInfoRequest userInfo;
}
