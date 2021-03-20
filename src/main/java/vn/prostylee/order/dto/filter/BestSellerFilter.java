package vn.prostylee.order.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.core.dto.filter.PagingParam;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BestSellerFilter extends PagingParam {

    /**
     * The default number of days that will be count for considering as best-seller products.
     */
    public static final int DEFAULT_TIME_RANGE_IN_DAYS = 90;

    private Long storeId;

    @Builder.Default
    @Schema(name = "timeRangeInDays", example = "365", description = "The number of days that will be count for considering as best-seller products.")
    @Range(min = 1, max = 365)
    private int timeRangeInDays = DEFAULT_TIME_RANGE_IN_DAYS;
}
