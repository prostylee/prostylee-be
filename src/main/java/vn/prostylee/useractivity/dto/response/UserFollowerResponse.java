package vn.prostylee.useractivity.dto.response;

import lombok.Data;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.store.dto.response.StoreResponse;

import java.io.Serializable;
import java.lang.annotation.Target;
import java.util.Date;

@Data
public class UserFollowerResponse implements Serializable {

    private Long id;

    private Date createdAt;

    private Long createdBy;

    private Long targetId;

    private TargetType targetType;

    private StoreResponse store;

    private UserResponse user;

    private Long customFieldId1;

    private Long customFieldId2;

    private Long customFieldId3;
}
