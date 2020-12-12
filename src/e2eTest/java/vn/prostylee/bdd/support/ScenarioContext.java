package vn.prostylee.bdd.support;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@Scope("cucumber-glue")
public class ScenarioContext<T> {

    private final Map<String, T> context = new HashMap<>();

    /**
     * Associates the specified value with the specified key in this map
     *  (optional operation).  If the map previously contained a mapping for
     *  the key, the old value is replaced by the specified value.
     *
     * @param key the key with which the specified value is to be associated.
     * @param value the value to be associated with the specified key.
     */
    public void get(String key, T value) {
        context.put(key, value);
    }

    /**
     * Returns the value to which the specified key is mapped,
     *      or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or
     *      {@code null} if this map contains no mapping for the key.
     */
    public T get(String key) {
        return context.get(key);
    }

    /**
     * Returns the value to which the specified key is mapped,
     *      or {@code null} if this map contains no mapping for the key.
     *
     * @param key the key whose mapping is to be removed from the map.
     * @return the previous value associated with <tt>key</tt>,
     *      or <tt>null</tt> if there was no mapping for <tt>key</tt>.
     */
    public T remove(String key) {
        return context.remove(key);
    }
}
