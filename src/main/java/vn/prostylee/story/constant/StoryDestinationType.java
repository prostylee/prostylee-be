package vn.prostylee.story.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum StoryDestinationType {
    USER("user"),
    STORE("store");

    @Getter
    private String type;
}
