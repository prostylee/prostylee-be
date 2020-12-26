package vn.prostylee.useractivity.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class UserRatingFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[0];
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
