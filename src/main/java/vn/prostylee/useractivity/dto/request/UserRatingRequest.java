package vn.prostylee.useractivity.dto.request;

import com.google.firebase.database.annotations.NotNull;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRatingRequest {
    @NotNull
    private Long targetId;

    @NotBlank
    private String targetType;

    private Integer value;
}
