package vn.prostylee.useractivity.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CheckFollowRequest {

    @NotNull
    private List<Long> targetIds;

    @NotBlank
    private String targetType;
}
