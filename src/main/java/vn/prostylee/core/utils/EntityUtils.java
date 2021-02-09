package vn.prostylee.core.utils;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class EntityUtils {

    private EntityUtils() {
    }

    /**
     * Merge sources to targets which identify via identifierFieldName
     *
     * @param sources The source items
     * @param targets The target items
     * @param identifierFieldName The identifier field name
     * @param <E> The source item type (Entity)
     * @param <R> The target item type (Dto)
     * @return The sources with items existing in both sources and targets, removing items not exists in targets, add items exists in targets only.
     */
    public static  <E, R> Set<E> merge(@NonNull final Set<E> sources, final Set<R> targets, @NonNull final String identifierFieldName, Class<E> sourceClazz) {
        if (targets == null) {
            return sources;
        }

        if (targets.isEmpty()) {
            sources.clear();
            return sources;
        }

        Set<R> adds = new HashSet<>();
        Set<Object> updateIds = new HashSet<>();
        Map<Object, R> updates = new HashMap<>();
        final int defaultIdentifierValue = -1;
        for (R obj: targets) {
            Object id = extractIdValue(obj, identifierFieldName, defaultIdentifierValue)  ;
            if (id == null) {
                adds.add(obj);
            } else if (defaultIdentifierValue != Long.parseLong(id.toString())){
                updateIds.add(id);
                updates.put(id, obj);
            }
        }

        Set<E> removes = sources.stream()
                .filter(obj -> !updateIds.contains(extractIdValue(obj, identifierFieldName, defaultIdentifierValue)))
                .collect(Collectors.toSet());

        sources.removeAll(removes);
        sources.forEach(obj -> {
            Object id = extractIdValue(obj, identifierFieldName, defaultIdentifierValue);
            if (updateIds.contains(id)) {
                BeanUtil.mergeProperties(updates.get(id), obj);
            }
        });
        adds.forEach(obj -> sources.add(BeanUtil.copyProperties(obj, sourceClazz)));
        return sources;
    }

    private static Object extractIdValue(Object obj, String fieldName, Object defaultValueIfError) {
        try {
            return FieldUtils.readDeclaredField(obj, fieldName, true)  ;
        } catch (IllegalAccessException e) {
            return defaultValueIfError;
        }
    }

}
