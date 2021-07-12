package vn.prostylee.post.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class PostStatisticResponse implements Serializable {

    private Long id;

    private Long numberOfLike;

    private Long numberOfComment;

}
