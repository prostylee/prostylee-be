package vn.prostylee.comment.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class CommentFilter extends BaseFilter {
    @Override
    public String[] getSearchableFields() {
        return new String[0];
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
