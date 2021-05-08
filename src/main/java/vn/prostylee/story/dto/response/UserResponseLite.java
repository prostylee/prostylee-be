package vn.prostylee.story.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserResponseLite implements Serializable {
    private Long id;
    private String fullName;
    private String avatar;
}