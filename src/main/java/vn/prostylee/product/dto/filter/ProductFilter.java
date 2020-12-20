package vn.prostylee.product.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class ProductFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "categoryId"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "price"
        };
    }
}
