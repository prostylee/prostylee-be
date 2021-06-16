package vn.prostylee.voucher.dto.filter;

import lombok.*;
import vn.prostylee.core.dto.filter.BaseFilter;

@AllArgsConstructor
@NoArgsConstructor
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
