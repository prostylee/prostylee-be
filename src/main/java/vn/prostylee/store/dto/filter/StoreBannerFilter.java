package vn.prostylee.store.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@EqualsAndHashCode(callSuper = true)
@Data
public class StoreBannerFilter extends BaseFilter {

    private Long storeId;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "description",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "description",
                "order",
        };
    }
}
