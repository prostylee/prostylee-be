package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttributeFilter extends BaseFilter {

    private String key;

    private Long categoryId;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "key",
                "label"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "key",
                "order",
                "label",
                "categoryId"
        };
    }
}
