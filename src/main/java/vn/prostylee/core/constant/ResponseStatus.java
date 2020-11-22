package vn.prostylee.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResponseStatus {

    SUCCESS("success"),
    ERROR("error");

    @Getter
    private String status;
}
