package vn.prostylee.voucher.dto.filter;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherFilter extends BaseFilter {

    private Long storeId;
    private Boolean active;
    private Integer type;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "code",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "description",
                "code",
                "type",
                "active",
                "createdAt",
        };
    }
}
