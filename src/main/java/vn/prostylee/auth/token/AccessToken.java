package vn.prostylee.auth.token;

import vn.prostylee.auth.dto.response.UserCredential;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Raw representation of JWT Token.
 */
@ToString
@AllArgsConstructor
public class AccessToken implements JwtToken {

    private final String token;

    @Getter
    private UserCredential subject;

    public String getToken() {
        return this.token;
    }
}
