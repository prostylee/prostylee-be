package vn.prostylee.post.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostFilter extends BaseFilter {

    private Long userId;
    private Long storeOwnerId;

    @Override
    public String[] getSearchableFields() {
        return new String[]{
                "description"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[]{
                "createdAt"
        };
    }
}
