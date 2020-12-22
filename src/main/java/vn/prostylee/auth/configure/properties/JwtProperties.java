package vn.prostylee.auth.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.security.jwt")
public class JwtProperties {

    /**
     * Key is used to sign JwtToken
     */
    private String tokenSigningKey;

    /**
     * JwtToken will expire after this time.
     */
    private int accessTokenExpirationInMs;

    /**
     * Use this token to get a new JwtToken when access token expired
     */
    private int refreshTokenExpirationInMs;

    /**
     * Token issuer
     */
    private String tokenIssuer;
}
