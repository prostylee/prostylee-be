package vn.prostylee.ads.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdvertisementCampaignResponse implements Serializable {

    private String name;

    private String description;

    private Long groupId;

    private Long featureImage;

    private String featureImageUrl;

    private String position;

    private Date fromDate;

    private Date toDate;

    private Double budget;

    private Long targetId;

    private String targetType;

    private Integer targetFromAge;

    private Integer targetToAge;

    private Long targetLocationId;

    private Boolean targetUserFollower;

    private Boolean targetUserLike;
}
