package vn.prostylee.order.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderFilter extends BaseFilter  {

    private String status;
    @JsonIgnore
    private Long loggedInUser;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "code"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "createdAt"
        };
    }
}
