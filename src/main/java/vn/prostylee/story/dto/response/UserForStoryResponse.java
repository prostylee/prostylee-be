package vn.prostylee.story.dto.response;

import lombok.Data;

@Data
public class UserForStoryResponse {
    private Long id;
    private String fullName;
    private String avatar;
}