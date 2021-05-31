package vn.prostylee.useractivity.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserWishListFilter extends BaseFilter {

    @Override
    public String[] getSearchableFields() {
        return new String[] {};
    }

    @Override
    public String[] getSortableFields() {
        return new String[] { "createdAt"};
    }
}
