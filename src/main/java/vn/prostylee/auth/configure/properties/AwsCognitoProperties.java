package vn.prostylee.auth.configure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.prostylee.core.configuration.properties.AwsCredentialProperties;

@Data
@Configuration
@ConfigurationProperties("app.aws.cognito")
public class AwsCognitoProperties {

    private AwsCredentialProperties credential;

    private String clientId;

    private String poolId;

    private String endpoint;

    private String region;

    private String identityPoolId;

    private String issuer;

    private String jwkUrl;

    private int connectionTimeout;

    private int readTimeout;
}
