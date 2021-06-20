package vn.prostylee.core.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public final class CollectionExtUtils {

    private CollectionExtUtils() {}

    public static <T> boolean contains(List<T> list, T num) {
        if (CollectionUtils.isEmpty(list) || num == null) {
            return false;
        }
        return list.stream().anyMatch(num::equals);
    }

    public static <T> boolean containsAny(List<T> list, List<T> others) {
        if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(others)) {
            return false;
        }
        return others.stream().anyMatch(item -> contains(list, item));
    }
}
