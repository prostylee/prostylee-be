package vn.prostylee.auth.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.security.reset-password-policies")
public class ResetPasswordPolicies {

    private Integer expiredInMinutes;

    private Integer length;

}
