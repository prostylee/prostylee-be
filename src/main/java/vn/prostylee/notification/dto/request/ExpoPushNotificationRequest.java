package vn.prostylee.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpoPushNotificationRequest implements PushNotificationRequest {

    @NotNull
    private String[] to;

    private String sound;

    @NotNull
    private String title;

    @NotNull
    private String body;

    private Map<String, Object> data;
}
