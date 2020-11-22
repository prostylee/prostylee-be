package vn.prostylee.notification.factory;

import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.dto.request.ExpoPushNotificationRequest;
import vn.prostylee.notification.dto.request.FcmPushNotificationRequest;
import vn.prostylee.notification.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

@Service
public class PushNotificationServiceFactory {

    private final PushNotificationService<ExpoPushNotificationRequest> expoPushNotificationService;
    private final PushNotificationService<FcmPushNotificationRequest> fcmPushNotificationService;

    private static final Map<NotificationProvider, PushNotificationService> CACHED_SERVICES = new EnumMap<>(NotificationProvider.class);

    @Autowired
    public PushNotificationServiceFactory(
            @Qualifier("expoPushNotificationService") PushNotificationService<ExpoPushNotificationRequest> expoPushNotificationService,
            @Qualifier("fcmPushNotificationService") PushNotificationService<FcmPushNotificationRequest> fcmPushNotificationService) {
        this.expoPushNotificationService = expoPushNotificationService;
        this.fcmPushNotificationService = fcmPushNotificationService;
    }

    @PostConstruct
    public void initCachedService() {
        CACHED_SERVICES.put(NotificationProvider.EXPO, expoPushNotificationService);
        CACHED_SERVICES.put(NotificationProvider.FIREBASE, fcmPushNotificationService);
    }

    public static PushNotificationService getService(NotificationProvider provider) {
        return CACHED_SERVICES.get(provider);
    }
}
