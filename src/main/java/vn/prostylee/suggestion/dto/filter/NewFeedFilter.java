package vn.prostylee.suggestion.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class NewFeedFilter extends BaseFilter {
    private Long userId;
}
