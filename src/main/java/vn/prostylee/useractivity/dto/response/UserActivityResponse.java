package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.util.Date;
@Data
public class UserActivityResponse {
    private Long id;
    private Date createdAt;
    private Long createdBy;
    private Long targetId;
    private String targetType;
}
