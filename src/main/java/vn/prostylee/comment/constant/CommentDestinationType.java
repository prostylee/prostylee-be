package vn.prostylee.comment.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommentDestinationType {

    PRODUCT("product"),
    STORE("store"),
    USER("user");

    @Getter
    private String type;
}
