package vn.prostylee.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Gender {
    MALE('M'), FEMALE('F'), OTHER('O');

    @Getter
    private Character value;
}
