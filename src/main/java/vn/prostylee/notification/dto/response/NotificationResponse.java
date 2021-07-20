package vn.prostylee.notification.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class NotificationResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private Integer type;

    private transient Map<String, Object> data;

    private transient NotificationSender sender;

    private Boolean markAsRead;

    private LocalDateTime createdAt;
}
