package vn.prostylee.ads.dto.response;

import lombok.Data;
import vn.prostylee.core.constant.TargetType;

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

    private TargetType targetType;

    private Integer targetFromAge;

    private Integer targetToAge;

    private Long targetLocationId;

    private Boolean targetUserFollower;

    private Boolean targetUserLike;
}
