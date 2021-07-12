package vn.prostylee.useractivity.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLikeFilter extends BaseFilter {

    private Long targetId;
    private TargetType targetType;
    private String userId;

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "createdBy",
                "targetId",
                "createdAt"
        };
    }
}
