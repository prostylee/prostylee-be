package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.thread-executor")
public class ThreadExecutorProperties {

    private int corePoolSize;

    private int maxPoolSize;

    private int queueCapacity;
}
