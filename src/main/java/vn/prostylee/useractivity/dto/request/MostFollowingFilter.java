package vn.prostylee.useractivity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.dto.filter.PagingParam;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MostFollowingFilter extends PagingParam {

    /**
     * The default number of days that will be count for considering as top following.
     */
    public static final int DEFAULT_TIME_RANGE_IN_DAYS = 90;

    private TargetType targetType;

    @Builder.Default
    @Schema(name = "timeRangeInDays", example = "365", description = "The number of days that will be count for considering as as top following.")
    @Range(min = 1, max = 365)
    private int timeRangeInDays = DEFAULT_TIME_RANGE_IN_DAYS;
}