package vn.prostylee.core.validator;

import java.util.Map;

/**
 * This interface for checking whether a entity exists in database based on id of entity and list of key and value of entity
 *
 * @param <ID> The type of entity's id
 */
@FunctionalInterface
public interface EntityExists<ID> {

    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param id The id of entity
     * @param uniqueValues The list of key and value of the fields for which to check if the value exists.
     * @return True if the value exists for the field; false otherwise
     */
    boolean isEntityExists(ID id, Map<String, Object> uniqueValues);
}
