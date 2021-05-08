package vn.prostylee.post.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostForListResponse extends PostResponse implements Serializable {
    private List<String> imageUrls;
}
