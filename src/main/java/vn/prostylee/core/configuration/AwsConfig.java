package vn.prostylee.core.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.prostylee.core.configuration.properties.AwsCredentialProperties;

@RequiredArgsConstructor
@Configuration
public class AwsConfig {

    private final AwsCredentialProperties credentialProperties;

    @Bean
    public AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(credentialProperties.getAccessKey(), credentialProperties.getSecretKey());
    }
}
