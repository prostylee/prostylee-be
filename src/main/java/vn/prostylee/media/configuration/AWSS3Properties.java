package vn.prostylee.media.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.aws")
public class AWSS3Properties {

    /**
     * S3 bucket used to store media files
     */
    private String bucket;

    /**
     * Region of bucket
     */
    private String region;

    /**
     * Access key
     */
    private String accessKey;

    /**
     * Secret key
     */
    private String secretKey;

    /**
     * AWS Hostname
     */
    private String hostname;
}