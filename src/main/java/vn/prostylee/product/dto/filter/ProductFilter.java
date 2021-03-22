package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.constant.NewFeedType;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductFilter extends BaseFilter {

    private Long categoryId;
    private Long storeId;
    private Long userId;
    private String size;
    private String status;
    private String material;
    private String style;

    private NewFeedType newFeedType = NewFeedType.STORE;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "category.name"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "createdAt",
                "priceSale",
                "price",
                "category.name"
        };
    }
}
