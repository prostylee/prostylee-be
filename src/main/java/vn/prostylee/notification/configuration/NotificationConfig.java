package vn.prostylee.notification.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import vn.prostylee.core.exception.MissingConfigurationException;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.service.PushNotificationService;

import java.util.List;

@Slf4j
@Configuration
public class NotificationConfig {

    @Primary
    @Bean
    public PushNotificationService pushNotificationService(
            List<PushNotificationService> pushNotificationServices,
            PushNotificationProperties pushNotificationProperties) {
        log.info("Using push notification provider = " + pushNotificationProperties.getProvider());
        return pushNotificationServices.stream()
                .filter(pushNotificationService -> pushNotificationService.getProvider() == findProvider(pushNotificationProperties.getProvider()))
                .findFirst()
                .orElse(null);
    }

    private NotificationProvider findProvider(String providerName) {
        return NotificationProvider.findProvider(providerName)
                .orElseThrow(() -> new MissingConfigurationException("Missing or wrong configuration for push notification provider = " + providerName));
    }
}
