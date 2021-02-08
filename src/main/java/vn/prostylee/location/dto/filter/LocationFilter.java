package vn.prostylee.location.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class LocationFilter extends BaseFilter {

    @Override
    public String[] getSearchableFields() {
        return new String[]{
                "address",
                "state",
                "city",
                "country",
                "zipcode"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[]{
                "address",
                "state",
                "city",
                "country",
                "zipcode"
        };
    }
}
