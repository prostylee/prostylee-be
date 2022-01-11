package vn.prostylee.comment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppSyncConfig {

    @Bean
    public WebClient.RequestBodySpec create(AwsAppSyncProperties awsAppSyncProperties) {
        return WebClient
                .builder()
                .baseUrl(awsAppSyncProperties.getEndpoint())
                .defaultHeader("x-api-key", awsAppSyncProperties.getApiKey())
                .build()
                .method(HttpMethod.POST)
                .uri("/graphql");
    }
}
