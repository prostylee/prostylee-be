package vn.prostylee.useractivity.dto.filter;

import lombok.*;
import vn.prostylee.core.constant.TargetType;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowingFilter extends UserFollowerPageable {

    @NotBlank
    private TargetType targetType;
}