package vn.prostylee.core.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.InputStream;

public final class JsonUtils {

    private JsonUtils() {
        super();
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T toObject(String file, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(getResourceAsStream(file), clazz);
    }

    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(json, clazz);
    }

    private static InputStream getResourceAsStream(String file) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(file);
    }
}
