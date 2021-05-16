package vn.prostylee.core.configuration.monitor.workflow;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public enum MutationAction {
    ALL(new String[0]),
    CREATE(new String[]{"save"}),
    UPDATE(new String[]{"update"}),
    DELETE(new String[]{"deleteById"}),
    NONE(new String[0]);

    @Getter
    private final String[] methodNames;

    public static Optional<MutationAction> findSupportedAction(String action) {
        return Stream.of(MutationAction.values())
                .filter(mutationAction -> Arrays.asList(mutationAction.getMethodNames()).contains(action))
                .findFirst();
    }
}
