package vn.prostylee.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public enum AppClientType {
    SYS_MOBILE("SYS_MOBILE", true),
    SYS_WEB("SYS_WEB", true),
    ;

    @Getter
    private String type;

    @Getter
    private boolean isLoginOnly;

    public static Optional<AppClientType> getByType(String type) {
        return Stream.of(AppClientType.values())
                .filter(appClientType -> appClientType.getType().equals(type))
                .findAny();
    }

    public static List<String> getAppClientLoginTypes() {
        return Stream.of(AppClientType.values())
                .filter(AppClientType::isLoginOnly)
                .map(AppClientType::getType)
                .collect(Collectors.toList());
    }
}
