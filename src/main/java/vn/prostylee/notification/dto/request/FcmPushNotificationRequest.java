package vn.prostylee.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcmPushNotificationRequest implements PushNotificationRequest {

    @NotNull
    private List<String> tokens;

    @NotNull
    private String title;

    @NotNull
    private String body;

    private Map<String, Object> data;

    private String link;
}
