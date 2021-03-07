package vn.prostylee.post.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class PostRequest {

    @Size(max = 4096)
    private String description;

    @NotNull
    private Long storeId;

    private List<PostImageRequest> postImageRequests;

    private List<Long> attachmentDeleteIds;
}
