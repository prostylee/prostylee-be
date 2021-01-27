package vn.prostylee.story.dto.filter;

import lombok.Data;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
public class StoryImageFilter extends BaseFilter {
    private Long storyId;

    @Override
    public String[] getSearchableFields() {
        return new String[0];
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
