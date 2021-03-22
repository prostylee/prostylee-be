package vn.prostylee.core.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PagingAndSortingParam extends PagingParam implements SortingDefinitions {

    /**
     * Multiple sort request parameters.
     * For example: sorts=+firstName&sorts=-lastName
     */
    private String[] sorts;
}
