package vn.prostylee.notification.dto.request;

import vn.prostylee.core.utils.BeanUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class FcmSubscriptionRequest extends FcmPushNotificationRequest {

    private String topicName;

    public void withParentBuilder(FcmPushNotificationRequest.FcmPushNotificationRequestBuilder parentBuilder) {
        BeanUtil.mergeProperties(parentBuilder.build(), this);
    }
}