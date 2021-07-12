package vn.prostylee.ads.dto.request;

import lombok.Data;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.location.dto.request.LocationRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class AdvertisementCampaignRequest {

    @NotBlank
    @Size(max = 512)
    private String name;

    @Size(max = 4096)
    private String description;

    @NotNull
    private Long groupId;

    @Valid
    private ImageRequest featureImageInfo;

    @Size(max = 128)
    private String position;

    private Date fromDate;

    private Date toDate;

    private Double budget;

    private Long targetId;

    @Size(max = 512)
    private TargetType targetType;

    private Integer targetFromAge;

    private Integer targetToAge;

    @Valid
    private LocationRequest targetLocation;

    private Boolean targetUserFollower;

    private Boolean targetUserLike;

}
