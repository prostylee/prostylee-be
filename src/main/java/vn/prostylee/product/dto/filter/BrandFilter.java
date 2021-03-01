package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class BrandFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "description"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
