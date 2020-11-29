package vn.prostylee.notification.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @NotNull
    private String title;

    @NotNull
    private String content;

    private Integer type;

    private Map<String, Object> data;

    private Boolean markAsRead;

    private Long userId;
}
