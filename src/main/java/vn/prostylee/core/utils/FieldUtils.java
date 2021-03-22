package vn.prostylee.core.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FieldUtils {

    private FieldUtils() {
    }

    public static <T> T readField(Class<T> clazz, final Object target, final String fieldName){
        Object obj = null;
        try {
            obj = org.apache.commons.lang3.reflect.FieldUtils.readField(target, fieldName, true);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.debug("Could not read field={} from class={}", fieldName, clazz.getCanonicalName(), e);
        }
        return clazz.cast(obj);
    }
}
