package vn.prostylee.core.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Define fields is allowed sorting
 */
public interface SortingDefinitions {

    @JsonIgnore
    String[] getSortableFields();
}
