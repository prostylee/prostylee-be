package vn.prostylee.media.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.prostylee.core.configuration.properties.AwsCredentialProperties;

@Data
@Configuration
@ConfigurationProperties("app.aws.s3")
public class AwsS3Properties {

    private AwsCredentialProperties credential;

    /**
     * S3 bucket used to store media files
     */
    private String bucket;

    /**
     * Region of bucket
     */
    private String region;

    /**
     * AWS S3 bucket url
     */
    private String bucketUrl;

    /**
     * AWS cloudfront id
     */
    private String cloudFrontId;

    /**
     * AWS cloudfront url
     */
    private String cloudFrontUrl;

    /**
     * AWS S3 bucket public folder name
     */
    private String s3MediaPublicFolder;

    /**
     * AWS Cloudfront resize image prefix
     */
    private String s3ResizeImagePrefix;
}
