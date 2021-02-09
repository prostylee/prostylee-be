package vn.prostylee.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSize {

    SMALL(90, 120),
    MEDIUM(375, 375),
    ;

    private final int width;
    private final int height;
}
