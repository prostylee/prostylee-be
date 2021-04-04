package vn.prostylee.product.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.constant.NewFeedType;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductFilter extends BaseFilter {

    private Long userId;

    private Long storeId;

    private NewFeedType newFeedType;

    private Boolean bestSeller;

    /**
     * The default number of days that will be count for considering as top following.
     */
    public static final int DEFAULT_TIME_RANGE_IN_DAYS = 90;

    @Builder.Default
    @Schema(name = "timeRangeInDays", example = "365", description = "The number of days that will be count for considering as as top following.")
    @Range(min = 1, max = 365)
    private int timeRangeInDays = DEFAULT_TIME_RANGE_IN_DAYS;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "price",
                "createdAt"
        };
    }

    @Schema(name = "sorts", example = "sorts=+name&sorts=-price&sorts=createdAt")
    @Override
    public String[] getSorts() {
        return super.getSorts();
    }
}
