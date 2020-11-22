package vn.prostylee.core.validator;

import org.springframework.lang.Nullable;

/**
 * This interface for checking whether a value exists in a field
 */
@FunctionalInterface
public interface FieldValueExists {

    /**
     * Checks whether or not a given value exists for a given field
     *
     * @param fieldName The name of the field for which to check if the value exists. This value maybe null or empty.
     * @param value The value to check for
     * @return True if the value exists for the field; false otherwise
     */
    boolean isFieldValueExists(@Nullable String fieldName, Object value);
}
