package vn.prostylee.notification.event.push;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.notification.constant.EmailTemplateType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationEventDto<T> {

    private EmailTemplateType emailTemplateType;

    private String email;

    private T data;

}
