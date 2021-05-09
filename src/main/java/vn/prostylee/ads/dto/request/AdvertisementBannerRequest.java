package vn.prostylee.ads.dto.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AdvertisementBannerRequest {

    @NotBlank
    @Size(max = 512)
    private String name;

    @Size(max = 4096)
    private String description;

    @NotNull
    private Long groupId;

    @Valid
    private ImageRequest bannerImageInfo;

    @Size(max = 2048)
    private String link;

    private Integer order;

}
