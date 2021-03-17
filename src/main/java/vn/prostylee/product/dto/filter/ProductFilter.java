package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.constant.NewFeedType;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductFilter extends BaseFilter {

    private Long userId;

    private Long storeId;

    private NewFeedType newFeedType = NewFeedType.STORE;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "price",
                "createdAt"
        };
    }
}
