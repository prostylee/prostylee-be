package vn.prostylee.post.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostForListResponse extends PostResponse{
    private List<String> imageUrls;
}
