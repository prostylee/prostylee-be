package vn.prostylee.useractivity.dto.filter;

import lombok.*;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.useractivity.constant.TrackingType;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTrackingFilter extends PagingParam {

    private TrackingType trackingType;

    private Long userId;
}