package vn.prostylee.media.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageSize {

    LOGO(46, 46),
    TINY(48, 64),
    EXTRA_SMALL(90, 120),
    STORY_SMALL(90, 140),
    STORY_LARGE(375,812),
    MEDIUM(375, 375),
    LARGE(375,812),
    FULL(0,0),
    PRODUCT_SIZE(600,900),
    POST_SIZE(600,750)
    ;

    private final int width;
    private final int height;
}
