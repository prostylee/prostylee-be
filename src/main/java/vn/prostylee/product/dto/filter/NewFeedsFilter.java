package vn.prostylee.product.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import vn.prostylee.core.dto.filter.BaseFilter;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewFeedsFilter extends BaseFilter {

    private List<Long> storeIds;
    private List<Long> userIds;
    private String targetType;

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
                "price"
        };
    }

    @Schema(name = "sorts", example = "sorts=+name&-price&createdAt")
    @Override
    public String[] getSorts() {
        return super.getSorts();
    }
}
