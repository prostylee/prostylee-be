package vn.prostylee.useractivity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class UserActivityRequest {
    private Date createdAt;

    private Long createdBy;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    private Long userId;
}
