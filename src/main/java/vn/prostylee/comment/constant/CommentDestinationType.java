package vn.prostylee.comment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommentDestinationType {

    PRODUCT("product"),
    STORE("store");

    @Getter
    private String status;
}
