package vn.prostylee.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSender {

    private Long storeId;

    private Long userId;

    private String name;

    private String avatar;
}
