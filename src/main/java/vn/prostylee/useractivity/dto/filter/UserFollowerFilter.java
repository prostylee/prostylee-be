package vn.prostylee.useractivity.dto.filter;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserFollowerFilter extends BaseFilter {

    private Long targetId;
    private String targetType;
    private Long userId;

    @Override
    public String[] getSearchableFields() {
        return new String[0];
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "createdBy",
                "targetId",
                "createdAt"
        };
    }
}
