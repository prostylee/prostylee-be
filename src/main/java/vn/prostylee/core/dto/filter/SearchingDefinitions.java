package vn.prostylee.core.dto.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Define fields is allowed searching
 */
public interface SearchingDefinitions {

    @JsonIgnore
    String[] getSearchableFields();
}
