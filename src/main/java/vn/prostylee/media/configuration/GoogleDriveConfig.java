package vn.prostylee.media.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import vn.prostylee.core.configuration.properties.GoogleDriveProperties;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration for Google drive
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class GoogleDriveConfig {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Arrays.asList("profile", "email", DriveScopes.DRIVE);

    private final ResourceLoader resourceLoader;

    private final GoogleDriveProperties googleDriveProperties;

    private GoogleCredentials googleCredential;

    @Bean
    public Drive loadGoogleDrive() {
        try {
            authorize();
            return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(googleCredential))
                    .setApplicationName(googleDriveProperties.getAppName())
                    .build();
        } catch (Exception ex) {
            log.error("LoadGoogleDrive Google API exception: ", ex);
            return null;
        }
    }

    /**
     * Oauth2 Google
     *
     */
    private void authorize() throws IOException {
        if(googleCredential == null)
            googleCredential = GoogleCredentials.fromStream(resourceLoader.getResource(googleDriveProperties.getServiceAccountCredential())
                    .getInputStream())
                    .createScoped(SCOPES);

        if(googleCredential.getAccessToken() == null) {
            googleCredential.refresh();
            googleCredential.refreshAccessToken();
        }
    }
}
