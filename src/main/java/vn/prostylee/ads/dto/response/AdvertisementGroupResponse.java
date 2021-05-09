package vn.prostylee.ads.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdvertisementGroupResponse implements Serializable {

    private String name;

    private String description;

    private String position;

    private Boolean active;

    private Date updatedAt;

    private Long updatedBy;

    private Date createdAt;

    private Long createdBy;
}
