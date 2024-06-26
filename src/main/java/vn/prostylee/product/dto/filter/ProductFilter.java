package vn.prostylee.product.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.core.dto.filter.BaseFilter;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilter extends BaseFilter {

    private Long userId;

    private Long productId;
    private Long categoryId;
    private Long storeId;
    private Long brandId;
    private List<Long> storeIds;
    private Map<String, List<String>> attributes;

    private Boolean topFollowingStore;
    private Boolean paidStore;
    private Boolean newStore;

    private Boolean bestSeller;
    private Boolean bestRating;
    private Boolean sale;

    private Double latitude;
    private Double longitude;

    private Double minPrice;
    private Double maxPrice;

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
                "name",
                "category.name"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "createdAt",
                "priceSale",
                "price"
        };
    }

    @Schema(name = "sorts", example = "sorts=+name&-price&createdAt")
    @Override
    public String[] getSorts() {
        return super.getSorts();
    }
}
