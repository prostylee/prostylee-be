package vn.prostylee.location.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LocationFilter extends BaseFilter {

    private List<Long> ids;

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
