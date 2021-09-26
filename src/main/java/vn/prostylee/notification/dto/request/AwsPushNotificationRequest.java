package vn.prostylee.notification.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwsPushNotificationRequest implements PushNotificationRequest {

    @NotNull
    private List<String> tokens;

    @NotNull
    private String title;

    @NotNull
    private String body;

    private Map<String, Object> data;

    private String link;
}
