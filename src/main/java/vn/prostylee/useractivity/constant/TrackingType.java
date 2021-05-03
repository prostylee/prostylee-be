package vn.prostylee.useractivity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum TrackingType {

    PRODUCT("/products"),
    STORE("/stores"),
    CATEGORY("/categories");

    @Getter
    private final String relativeApiPath;

    public static Optional<TrackingType> find(String trackingTypeName) {
        if (StringUtils.isBlank(trackingTypeName)) {
            return Optional.empty();
        }
        return Stream.of(TrackingType.values())
                .filter(trackingType -> trackingType.name().equalsIgnoreCase(trackingTypeName))
                .findFirst();
    }
}
