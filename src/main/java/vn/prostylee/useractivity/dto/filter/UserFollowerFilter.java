package vn.prostylee.useractivity.dto.filter;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerFilter extends UserFollowerPageable {

    private String targetType;
    private Long targetId;
    private Long userId;
}
