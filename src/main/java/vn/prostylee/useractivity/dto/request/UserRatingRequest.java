package vn.prostylee.useractivity.dto.request;

import lombok.Data;

@Data
public class UserRatingRequest {
    private Long targetId;
    private String targetType;
    private Integer value;
}
