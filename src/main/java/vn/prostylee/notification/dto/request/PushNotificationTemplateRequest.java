package vn.prostylee.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationTemplateRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String type;
}
