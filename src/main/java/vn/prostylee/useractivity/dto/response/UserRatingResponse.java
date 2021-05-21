package vn.prostylee.useractivity.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.response.AuditResponse;
import vn.prostylee.story.dto.response.UserResponseLite;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRatingResponse extends AuditResponse {

    private Long id;

    private Long targetId;

    private String targetType;

    private Integer value;

    private String content;

    private List<UserRatingImageResponse> images;

    private UserResponseLite user;
}
