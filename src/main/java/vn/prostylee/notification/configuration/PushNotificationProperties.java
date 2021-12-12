package vn.prostylee.notification.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.push-notification")
public class PushNotificationProperties {

    private String provider;

    private String url;

}
