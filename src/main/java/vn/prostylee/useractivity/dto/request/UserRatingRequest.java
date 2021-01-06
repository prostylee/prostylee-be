package vn.prostylee.useractivity.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRatingRequest {
    @NotNull
    private Long targetId;

    @NotBlank
    private String targetType;

    private Integer value;
}
