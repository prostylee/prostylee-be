package vn.prostylee.voucher.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.entity.Attribute;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VoucherFilter extends BaseFilter {

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name"
        };
    }
}
