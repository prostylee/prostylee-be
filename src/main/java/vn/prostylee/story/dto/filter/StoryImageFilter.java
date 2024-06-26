package vn.prostylee.story.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;

@Data
@EqualsAndHashCode(callSuper=false)
public class StoryImageFilter extends BaseFilter {
    private Long storyId;
}
