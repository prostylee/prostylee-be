package vn.prostylee.notification.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.core.exception.MissingConfigurationException;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.service.PushNotificationService;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PushNotificationServiceFactory {

    private static final Map<NotificationProvider, PushNotificationService> CACHED_SERVICES = new EnumMap<>(NotificationProvider.class);

    @Autowired
    public PushNotificationServiceFactory(List<PushNotificationService> pushNotificationServices) {
        pushNotificationServices.forEach(pushNotificationService -> {
            CACHED_SERVICES.put(pushNotificationService.getProvider(), pushNotificationService);
        });
    }

    public static PushNotificationService getService(String providerName) {
        return NotificationProvider.findProvider(providerName)
                .map(PushNotificationServiceFactory::getService)
                .orElseThrow(() -> new MissingConfigurationException("Missing or wrong configuration for push notification provider = " + providerName));
    }

    public static PushNotificationService getService(NotificationProvider provider) {
        log.debug("CACHED_SERVICES={}, NotificationProvider={}", CACHED_SERVICES.size(), provider);
        return CACHED_SERVICES.get(provider);
    }
}
