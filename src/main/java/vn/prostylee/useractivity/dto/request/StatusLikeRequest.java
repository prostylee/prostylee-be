package vn.prostylee.useractivity.dto.request;

import lombok.Builder;
import lombok.Data;
import vn.prostylee.core.constant.TargetType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class StatusLikeRequest {

    @NotNull
    private List<Long> targetIds;

    @NotNull
    private TargetType targetType;

}
