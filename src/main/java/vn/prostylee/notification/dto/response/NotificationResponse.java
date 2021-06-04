package vn.prostylee.notification.dto.response;

import lombok.Data;
import vn.prostylee.auth.dto.response.BasicUserResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class NotificationResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private Integer type;

    private Map<String, Object> data;

    private BasicUserResponse sendFrom;

    private Boolean markAsRead;

    private LocalDateTime createdAt;
}
