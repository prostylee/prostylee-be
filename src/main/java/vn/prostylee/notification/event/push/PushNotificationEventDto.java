package vn.prostylee.notification.event.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.notification.constant.PushNotificationTemplateType;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationEventDto<T> {

    private PushNotificationTemplateType templateType;

    private String provider;

    private List<Long> userIds;

    private List<Long> storeIds;

    private T fillData;

    private Map<String, Object> pushData;

}
