package vn.prostylee.story.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreForStoryResponse implements Serializable {
    private Long id;
    private String name;
    private String logoUrl;
}
