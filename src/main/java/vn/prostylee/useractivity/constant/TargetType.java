package vn.prostylee.useractivity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TargetType {
    PRODUCT("product"),
    STORE("store");

    @Getter
    private String status;
}
