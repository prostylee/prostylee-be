package vn.prostylee.media.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This holds aws configurations.
 */
@Configuration
public class AwsS3Config {

    private AWSS3Properties awsS3Properties;

    @Autowired
    public AwsS3Config(AWSS3Properties awsS3Properties) {
        this.awsS3Properties = awsS3Properties;
    }

    @Bean
    public AmazonS3 awsS3Client() {
        AWSCredentials credentials =
                new BasicAWSCredentials(awsS3Properties.getAccessKey(),awsS3Properties.getSecretKey());
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsS3Properties.getRegion())
                .build();
    }
}
