package vn.prostylee.shipping.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShippingMethodFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "id",
                "name",
                "description"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[]{
                "name",
                "description",
                "createdAt"
        };
    }
}
