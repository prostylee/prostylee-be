package vn.prostylee.notification.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubscribePushNotificationRequest {

    @NotNull
    private String token;

    private String deviceName;

    private String deviceId;

    private String software;

    private String osVersion;

    private String osName;

    private String brand;

    private String manufacturer;

    private String modelName;
}
