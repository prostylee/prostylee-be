package vn.prostylee.media.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class MediaRequest {

    @NotBlank
    @Size(max = 512)
    private String name;

    @NotBlank
    @Size(max = 2048)
    private String path;
}