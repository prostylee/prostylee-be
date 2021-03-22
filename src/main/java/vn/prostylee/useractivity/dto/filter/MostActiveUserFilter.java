package vn.prostylee.useractivity.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class MostActiveUserFilter {

    /**
     * The default number of days that will be count for considering as most active users.
     */
    public static final int DEFAULT_TIME_RANGE_IN_DAYS = 30;

    @Schema(name = "limit", example = "20", description = "Number of records to be received.")
    @Range(min = 1, max = 100)
    private int limit = 20;

    @Schema(name = "timeRangeInDays", example = "30", description = "The number of days that will be count for considering as most active users.")
    @Range(min = 1, max = 365)
    private int timeRangeInDays = DEFAULT_TIME_RANGE_IN_DAYS;
}
