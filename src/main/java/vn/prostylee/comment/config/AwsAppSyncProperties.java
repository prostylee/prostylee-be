package vn.prostylee.comment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.aws.appsync")
public class AwsAppSyncProperties {

	private String endpoint;

	private String apiKey;
}
