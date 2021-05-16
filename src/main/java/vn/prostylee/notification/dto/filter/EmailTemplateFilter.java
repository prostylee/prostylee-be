package vn.prostylee.notification.dto.filter;

import vn.prostylee.core.dto.filter.BaseFilter;

public class EmailTemplateFilter extends BaseFilter {

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "title",
                "content",
                "type",
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "title",
                "content",
                "createdAt",
                "type",
        };
    }
}
