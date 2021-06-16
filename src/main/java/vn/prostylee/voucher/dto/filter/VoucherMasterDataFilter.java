package vn.prostylee.voucher.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherMasterDataFilter extends BaseFilter {

    private String group;
    private Boolean active;

    @Override
    public String[] getSearchableFields() {
        return new String[]{
                "name",
                "description",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[]{
                "name",
                "group",
                "description",
                "createdAt",
        };
    }

}
