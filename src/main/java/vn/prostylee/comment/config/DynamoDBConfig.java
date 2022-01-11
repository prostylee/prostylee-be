package vn.prostylee.comment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(AwsCredentialsProvider awsCredentialsProvider, AwsDynamoDbProperties awsDynamoDbProperties) {
        ClientOverrideConfiguration.Builder overrideConfig = ClientOverrideConfiguration.builder();
        return DynamoDbClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .overrideConfiguration(overrideConfig.build())
                .region(Region.of(awsDynamoDbProperties.getRegion()))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}