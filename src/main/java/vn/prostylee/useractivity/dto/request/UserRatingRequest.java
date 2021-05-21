package vn.prostylee.useractivity.dto.request;

import lombok.Data;
import vn.prostylee.media.dto.request.MediaRequest;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserRatingRequest {

    @NotNull
    private Long targetId;

    @NotBlank
    private String targetType;

    @NotNull
    @Max(5)
    @Min(1)
    private Integer value;

    @NotBlank
    private String content;

    @Valid
    private List<MediaRequest> images;
}
