package vn.prostylee.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwsPushNotificationRequest extends PushNotificationRequest {

    private String link;

    /**
     * Specifies whether the notification is a silent push notification, which is a push notification that doesn't display on a recipient's device.
     * Silent push notifications can be used for cases such as updating an app's configuration or supporting phone home functionality.
     */
    private Boolean silentPush;
}
