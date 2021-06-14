package vn.prostylee.useractivity.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class StatusFollowRequest {

    @NotNull
    private List<Long> targetIds;

    @NotBlank
    private String targetType;
}
