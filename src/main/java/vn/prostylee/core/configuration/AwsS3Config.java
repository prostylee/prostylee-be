package vn.prostylee.core.configuration;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This holds aws configurations.
 */
@Configuration
public class AwsS3Config {

    @Bean
    public AmazonS3 awsS3Client() {
        return AmazonS3ClientBuilder.standard().build();
    }
}
