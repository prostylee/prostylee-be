package vn.prostylee.core.utils;

public final class FieldUtils {

    private FieldUtils() {
    }

    public static <T> T readField(Class<T> clazz, final Object target, final String fieldName){
        Object obj = null;
        try {
            obj = org.apache.commons.lang3.reflect.FieldUtils.readField(target, fieldName, true);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return clazz.cast(obj);
    }
}
