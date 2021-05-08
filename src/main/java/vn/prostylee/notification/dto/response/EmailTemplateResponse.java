package vn.prostylee.notification.dto.response;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EmailTemplateResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private String type;

    private String keyword;

    private Long originalId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
