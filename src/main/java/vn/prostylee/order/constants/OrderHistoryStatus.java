package vn.prostylee.order.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum OrderHistoryStatus {

    AWAITING_CONFIRMATION(0),
    PROGRESS_FAIL(1),
    PROGRESS_SUCCESS(2);

    private final int completedStatus;

}
