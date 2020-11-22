package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("services.local-storage.local-folder")
public class LocalStorageFolderProperties {

    private String location;
}
