package vn.prostylee.useractivity.dto.filter;

import lombok.*;
import vn.prostylee.core.constant.TargetType;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerFilter extends UserFollowerPageable {

    /**
     * deprecated Using targetTypes
     */
    @Deprecated
    private TargetType targetType;

    private List<TargetType> targetTypes;

    private Long targetId;

    private Long userId;
}
