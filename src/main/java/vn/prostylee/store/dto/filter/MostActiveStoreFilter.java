package vn.prostylee.store.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.product.constant.PagingConstant;

@Data
public class MostActiveStoreFilter {

    /**
     * The default number of days that will be count for considering as most active stores.
     */
    public static final int DEFAULT_TIME_RANGE_IN_DAYS = 30;

    @Schema(name = "limit", example = "20", description = "Number of records to be received.")
    @Range(min = 1, max = 100)
    private int limit = 20;

    private int page = 0;

    @Schema(name = "numberOfProducts", example = "10", description = "Number of products in each store to be received.")
    @Range(min = 1, max = 100)
    private int numberOfProducts = PagingConstant.DEFAULT_NUMBER_OF_PRODUCT_IN_EACH_STORE;

    @Schema(name = "timeRangeInDays", example = "30", description = "The number of days that will be count for considering as most active stores.")
    @Range(min = 1, max = 365)
    private int timeRangeInDays = DEFAULT_TIME_RANGE_IN_DAYS;
}
