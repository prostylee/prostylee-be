package vn.prostylee.auth.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties("app.security")
public class SecurityProperties {

    private boolean allowCredentials;

    private long maxAge;

    private List<String> allowedOrigins;

    private List<String> allowedHeaders;

    private List<String> allowedMethods;

    private List<String> allowedPublicApis;

    private ResetPasswordPolicies resetPasswordPolicies;

    private JwtProperties jwt;
}