package vn.prostylee.notification.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class NotificationDiscountResponse {

    private Long id;

    private String title;

    private String content;

    private Map<String, Object> data;

    private NotificationSender sender;

    private LocalDateTime createdAt;
}
