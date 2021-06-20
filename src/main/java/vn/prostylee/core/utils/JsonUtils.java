package vn.prostylee.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;

public final class JsonUtils {

    private JsonUtils() {
        super();
    }

    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toObject(String file, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(getResourceAsStream(file), clazz);
    }

    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> clazz) {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    @SneakyThrows
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        return OBJECT_MAPPER.readValue(json, valueTypeRef);
    }

    private static InputStream getResourceAsStream(String file) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(file);
    }
}
