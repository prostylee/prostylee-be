package vn.prostylee.story.dto.request;

import lombok.Data;
import vn.prostylee.media.dto.request.MediaRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class StoryRequest {

    @NotNull
    private List<MediaRequest> images;

    private Long targetId;

    private String targetType;

    private Long productId;

    private Long storeId;
}
