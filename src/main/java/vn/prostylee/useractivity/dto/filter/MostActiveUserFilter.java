package vn.prostylee.useractivity.dto.filter;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class MostActiveUserFilter {

    @Range(min = 1, max = 100)
    private int limit = 20;

    @Range(min = 1, max = 365)
    private int timeRangeInDays = 30;
}
