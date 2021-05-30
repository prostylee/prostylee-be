package vn.prostylee.store.dto.request;


import lombok.Data;
import vn.prostylee.ads.dto.request.ImageRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class StoreBannerRequest {

    @NotBlank
    @Size(max = 512)
    private String name;

    @Size(max = 4096)
    private String description;

    @Valid
    private ImageRequest bannerImageInfo;

    @Size(max = 2048)
    private String link;

    private Integer order;

    @NotNull
    private Long storeId;

}

