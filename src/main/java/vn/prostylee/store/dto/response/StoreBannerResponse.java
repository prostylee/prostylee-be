package vn.prostylee.store.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreBannerResponse implements Serializable {

    private String id;

    private String name;

    private String description;

    private Long bannerImage;

    private String bannerImageUrl;

    private String link;

    private Integer order;

    private Long storeId;

}
