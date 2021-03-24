package vn.prostylee.auth.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserAddressFilter extends BaseFilter {

	private String cityCode;
	private String districtCode;
    private String wardCode;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "address",
                "fullAddress"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[]{
                "address",
                "fullAddress",
                "createdAt",
                "priority"
        };
    }
}