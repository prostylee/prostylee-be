package vn.prostylee.useractivity.dto.filter;

import vn.prostylee.core.dto.filter.PagingAndSortingParam;

public class UserActivityFilter extends PagingAndSortingParam {

    @Override
    public String[] getSortableFields() {

        return new String[] {
            "username",
            "fullName",
            "email"
        };
    }
}
