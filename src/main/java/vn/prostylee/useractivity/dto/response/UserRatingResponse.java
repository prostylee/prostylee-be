package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRatingResponse implements Serializable {
    private Long id;
    private Date createdAt;
    private Long createdBy;
    private Date updatedAt;
    private Long updatedBy;
    private Long targetId;
    private String targetType;
    private Integer value;
}
