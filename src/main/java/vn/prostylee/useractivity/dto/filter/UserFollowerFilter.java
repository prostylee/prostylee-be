package vn.prostylee.useractivity.dto.filter;

import lombok.*;
import vn.prostylee.core.constant.TargetType;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerFilter extends UserFollowerPageable {

    private TargetType targetType;
    private Long targetId;
    private Long userId;
}
