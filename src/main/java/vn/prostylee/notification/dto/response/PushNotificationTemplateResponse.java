package vn.prostylee.notification.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.response.AuditResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class PushNotificationTemplateResponse extends AuditResponse {

    private Long id;

    private String title;

    private String content;

    private String type;
}
