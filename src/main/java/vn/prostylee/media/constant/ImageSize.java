package vn.prostylee.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSize {

    LOGO(46, 46),
    TINY(48, 64),
    EXTRA_SMALL(90, 120),
    SMALL(90, 140),
    MEDIUM(375, 375),
    LARGE(375,812)
    ;

    private final int width;
    private final int height;
}
