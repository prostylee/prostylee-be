package vn.prostylee.media.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This holds AWS S3 configurations.
 */
@RequiredArgsConstructor
@Configuration
public class AwsS3Config {

    private final AwsS3Properties awsS3Properties;

    @Bean
    public AmazonS3 awsS3Client(AWSCredentials credentials) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsS3Properties.getRegion())
                .build();
    }
}
