package vn.prostylee.core.dto.filter;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public abstract class PagingAndSortingParam implements SortingDefinitions {

    @Min(value = 0)
    private int page = 0;

    @Min(value = 1)
    private int limit = 50;

    /**
     * Multiple sort request parameters.
     * For example: sorts=+firstName&sorts=-lastName
     */
    private String[] sorts;
}
