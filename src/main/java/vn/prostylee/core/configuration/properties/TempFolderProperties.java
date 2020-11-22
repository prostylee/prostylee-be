package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("services.local-storage.temp-folder")
public class TempFolderProperties {

    private String location;

    private int timeToLiveInHours;
}
