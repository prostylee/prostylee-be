package vn.prostylee.notification.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class NotificationResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String sendFrom;

    private Boolean markAsRead;

    private LocalDateTime createdAt;
}
