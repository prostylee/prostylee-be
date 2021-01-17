package vn.prostylee.store.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreFilter extends BaseFilter  {

    private Long companyId;

    private Integer status;

    private Long ownerId;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "address",
                "website",
                "phone"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "address",
                "website",
                "phone",
                "status",
        };
    }
}
