package vn.prostylee.useractivity.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserRatingFilter extends BaseFilter {

    private Long targetId;

    private TargetType targetType;

    private Integer value;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
          "content"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "value",
                "content",
                "createdAt"
        };
    }
}
