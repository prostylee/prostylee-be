package vn.prostylee.core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import vn.prostylee.core.configuration.properties.AwsCredentialProperties;

@RequiredArgsConstructor
@Configuration
public class AwsConfig {

    private final AwsCredentialProperties credentialProperties;

    @Bean
    public AwsBasicCredentials awsCredentials() {
        return AwsBasicCredentials.create(credentialProperties.getAccessKey(), credentialProperties.getSecretKey());
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(AwsBasicCredentials awsCredentials) {
        return StaticCredentialsProvider.create(awsCredentials);
    }
}
