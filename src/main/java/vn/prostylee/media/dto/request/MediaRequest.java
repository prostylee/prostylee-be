package vn.prostylee.media.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class MediaRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String path;
}