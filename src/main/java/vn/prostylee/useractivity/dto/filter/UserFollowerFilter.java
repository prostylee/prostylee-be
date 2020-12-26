package vn.prostylee.useractivity.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class UserFollowerFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "user_id",
                "target_id"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "user_id",
                "target_id",
                "created_at"
        };
    }
}
