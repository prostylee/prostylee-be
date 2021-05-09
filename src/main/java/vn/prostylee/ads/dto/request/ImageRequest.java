package vn.prostylee.ads.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ImageRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String path;

}
