package vn.prostylee.ads.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class AdvertisementGroupRequest {

    @NotBlank
    @Size(max = 512)
    private String name;

    @Size(max = 4096)
    private String description;

    @Size(max = 128)
    private String position;

    private Boolean active;
}
