package vn.prostylee.notification.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.prostylee.core.configuration.properties.AwsCredentialProperties;

@Data
@Configuration
@ConfigurationProperties("app.aws.pinpoint")
public class AwsPinpointProperties {

    private AwsCredentialProperties credential;

    /**
     * Region of notification
     */
    private String region;

}
