package vn.prostylee.media.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * This holds AWS S3 configurations.
 */
@RequiredArgsConstructor
@Configuration
public class AwsS3Config {

    private final AwsS3Properties awsS3Properties;

    @Bean
    public S3Client awsS3Client(AwsCredentialsProvider awsCredentialsProvider) {
        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.of(awsS3Properties.getRegion()))
                .build();
    }
}
