package vn.prostylee.story.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StoryRequest {

    @NotNull
    private List<Long> attachmentIds;

    private Long targetId;

    private String targetType;

    private Long productId;
}
