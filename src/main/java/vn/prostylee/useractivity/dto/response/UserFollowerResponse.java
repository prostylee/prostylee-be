package vn.prostylee.useractivity.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserFollowerResponse {

    private Long id;

    private Date createdAt;

    private Long createdBy;

    private Long targetId;

    private String targetType;

    private Long customFieldId1;

    private Long customFieldId2;

    private Long customFieldId3;
}
