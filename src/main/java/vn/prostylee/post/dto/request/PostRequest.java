package vn.prostylee.post.dto.request;

import lombok.Builder;
import lombok.Data;
import vn.prostylee.media.dto.request.MediaRequest;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class PostRequest {

    @Size(max = 4096)
    private String description;

    private Long storeId;

    private List<MediaRequest> images;

    private List<Long> attachmentDeleteIds;
}
