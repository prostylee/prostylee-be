package vn.prostylee.core.configuration.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.prostylee.core.configuration.logging.EventLogFilter;

@Configuration
public class LoggingConfig {

    @Bean
    public EventLogFilter eventLogFilter() {
        return new EventLogFilter();
    }
}
