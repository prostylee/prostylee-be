package vn.prostylee.auth.configure.security;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import vn.prostylee.auth.configure.properties.AwsCognitoProperties;

import java.net.MalformedURLException;
import java.net.URL;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

/**
 * This holds AWS Cognito configurations.
 */
@RequiredArgsConstructor
@Configuration
public class AwsCognitoConfig {

    private final AwsCognitoProperties awsCognitoProperties;

    @Bean
    public CognitoIdentityProviderClient awsCognitoIdentityProvider(AwsCredentialsProvider awsCredentialsProvider) {
        return CognitoIdentityProviderClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.of(awsCognitoProperties.getRegion()))
                .build();
    }

    @Bean
    public ConfigurableJWTProcessor<?> configurableJWTProcessor() throws MalformedURLException {
        ResourceRetriever resourceRetriever =
                new DefaultResourceRetriever(awsCognitoProperties.getConnectionTimeout(),
                        awsCognitoProperties.getReadTimeout());
                        URL jwkSetUrl= new URL(awsCognitoProperties.getJwkUrl());

        JWKSource<?> keySource= new RemoteJWKSet<>(jwkSetUrl, resourceRetriever);
        ConfigurableJWTProcessor<?> jwtProcessor= new DefaultJWTProcessor<>();
        JWSKeySelector keySelector= new JWSVerificationKeySelector<>(RS256, keySource);
        jwtProcessor.setJWSKeySelector(keySelector);
        return jwtProcessor;
    }

}