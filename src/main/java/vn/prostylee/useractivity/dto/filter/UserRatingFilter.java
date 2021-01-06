package vn.prostylee.useractivity.dto.filter;

import lombok.Data;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
public class UserRatingFilter extends BaseFilter {
    private Long targetId;
    private String targetType;
    private Integer value;

    @Override
    public String[] getSearchableFields() {
        return new String[0];
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
