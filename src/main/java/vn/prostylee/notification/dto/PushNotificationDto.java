package vn.prostylee.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.prostylee.auth.dto.UserToken;
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
public class PushNotificationDto {

    @NotNull
    private List<UserToken> userTokens;

    @NotNull
    private Integer type;

    @NotNull
    private String title;

    @NotNull
    private String body;

    private Map<String, Object> data;

    /**
     * Is only used when we send a push notification via FCM
     */
    private String topicName;

    /**
     * Is only used when we send a push notification via FCM
     */
    private String link;
}
