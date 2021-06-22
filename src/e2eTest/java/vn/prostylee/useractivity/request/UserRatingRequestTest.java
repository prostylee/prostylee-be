package vn.prostylee.useractivity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.prostylee.media.dto.request.MediaRequest;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRatingRequestTest {
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