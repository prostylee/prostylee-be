package vn.prostylee.product.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.constant.NewFeedType;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductFilter extends BaseFilter {

    private Long userId;

    private Long storeId;

    private NewFeedType newFeedType;

    private Boolean bestSeller;

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

    @Schema(name = "sorts", example = "sorts=+name&sorts=-price&sorts=createdAt")
    @Override
    public String[] getSorts() {
        return super.getSorts();
    }
}
