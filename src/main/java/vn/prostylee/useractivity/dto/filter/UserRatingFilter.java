package vn.prostylee.useractivity.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper=true)
public class UserRatingFilter extends BaseFilter {
    private Long targetId;
    private String targetType;
    private Integer value;
}
