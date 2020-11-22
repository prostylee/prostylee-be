package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("services.drive")
public class GoogleDriveProperties {

    private String appName;

    private String folderId;

    private String serviceAccountCredential;
}
