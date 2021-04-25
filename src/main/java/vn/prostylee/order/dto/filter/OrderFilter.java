package vn.prostylee.order.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
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
