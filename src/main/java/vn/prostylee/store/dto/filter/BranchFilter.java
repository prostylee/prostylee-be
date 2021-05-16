package vn.prostylee.store.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class BranchFilter extends BaseFilter  {

    private Long storeId;

    private Boolean active;

    private Double latitude;

    private Double longitude;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "description",
                "active",
                "createdAt",
                "updatedAt",
        };
    }
}
