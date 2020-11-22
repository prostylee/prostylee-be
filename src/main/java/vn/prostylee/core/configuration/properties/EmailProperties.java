package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("services.email")
public class EmailProperties {

    private String receiveContact;

    private String replyTo;

    private String sendFrom;
}
