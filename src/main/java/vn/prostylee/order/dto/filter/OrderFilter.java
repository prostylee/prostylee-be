package vn.prostylee.order.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFilter extends BaseFilter  {

    private String status;
    private Long loggedInUser;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "code",
                "status"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "createdAt"
        };
    }
}
