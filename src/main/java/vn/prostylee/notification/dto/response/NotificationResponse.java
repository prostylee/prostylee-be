package vn.prostylee.notification.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;

    private String title;

    private String content;

    private String sendFrom;

    private Boolean markAsRead;

    private LocalDateTime createdAt;
}
