package vn.prostylee.core.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.prostylee.core.configuration.properties.CachingProperties;

@Configuration
@EnableCaching
public class CachingConfig {

    @Bean
    public Config config(CachingProperties cachingProperties) {
        Config config = new Config();

        MapConfig mapConfig = new MapConfig();
        mapConfig.setTimeToLiveSeconds(cachingProperties.getTimeToLiveInSeconds());

        config.getMapConfigs().put("global", mapConfig);

        return config;
    }
}
