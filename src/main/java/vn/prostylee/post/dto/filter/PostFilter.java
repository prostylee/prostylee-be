package vn.prostylee.post.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class PostFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[]{
          "description",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
