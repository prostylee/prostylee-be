package vn.prostylee.media.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * This holds aws configurations.
 */
@Configuration
@Component
public class AwsS3Config {

    @Value("${app.aws.accessKey}")
    private String accessKey;
    @Value("${app.aws.secretKey}")
    private String secretKey;
    @Value("${app.aws.region}")
    private String region;
    @Bean
    public AmazonS3 awsS3Client() {
        AWSCredentials credentials =
                new BasicAWSCredentials(accessKey,secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }
}
