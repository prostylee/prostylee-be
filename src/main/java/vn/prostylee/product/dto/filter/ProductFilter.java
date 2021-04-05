package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.constant.NewFeedType;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class  ProductFilter extends BaseFilter {

    private Long productId;
    private Long categoryId;
    private Long storeId;
    private Long userId;
    private Map<String, String> attributes;

    private NewFeedType newFeedType = NewFeedType.STORE;
    private Boolean bestSeller;

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

//    @Schema(name = "sorts", example = "sorts=+name&-price&createdAt")
    @Override
    public String[] getSorts() {
        return super.getSorts();
    }
}
