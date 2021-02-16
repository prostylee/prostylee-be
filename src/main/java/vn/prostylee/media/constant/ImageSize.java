package vn.prostylee.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSize {

    TINY(48, 64),
    SMALL(90, 120),
    MEDIUM(375, 375),
    LARGE(375,812)
    ;

    private final int width;
    private final int height;
}
