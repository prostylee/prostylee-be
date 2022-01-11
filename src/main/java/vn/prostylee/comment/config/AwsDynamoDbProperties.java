package vn.prostylee.comment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.aws.dynamodb")
public class AwsDynamoDbProperties {

	private String region;

	private String tableNameSuffix;
}
