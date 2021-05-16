package vn.prostylee.ads.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdvertisementBannerResponse implements Serializable {

    private String name;

    private String description;

    private Long groupId;

    private Long bannerImage;

    private String bannerImageUrl;

    private String link;

    private Integer order;

}
