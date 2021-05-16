package vn.prostylee.notification.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class PushNotificationTemplateDryRunRequest {

    @NotBlank
    private String token;

    @Schema(description = "One of FIREBASE, EXPO")
    private String provider;

    private Map<String, Object> fillData;

    private Map<String, Object> pushData;
}
