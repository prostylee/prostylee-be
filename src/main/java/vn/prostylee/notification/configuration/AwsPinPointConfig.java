package vn.prostylee.notification.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.pinpoint.PinpointClient;

@Configuration
public class AwsPinPointConfig {

    @Bean
    public PinpointClient pinpointClient(AwsCredentialsProvider awsCredentialsProvider, AwsPinpointProperties awsPinpointProperties) {
        return PinpointClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.of(awsPinpointProperties.getRegion()))
                .build();
    }
}
