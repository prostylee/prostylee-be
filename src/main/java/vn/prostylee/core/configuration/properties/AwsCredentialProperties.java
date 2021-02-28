package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.aws.credential")
public class AwsCredentialProperties {

	private String accessKey;

	private String secretKey;
}
