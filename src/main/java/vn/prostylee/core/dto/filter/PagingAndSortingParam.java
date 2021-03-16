package vn.prostylee.core.dto.filter;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public abstract class PagingAndSortingParam implements SortingDefinitions {

    public static final int NUMBER_OF_RECORD_DEFAULT = 30;

    @Min(value = 0)
    private int page = 0;

    @Min(value = 1)
    private int limit = NUMBER_OF_RECORD_DEFAULT;

    /**
     * Multiple sort request parameters.
     * For example: sorts=+firstName&sorts=-lastName
     */
    private String[] sorts;
}
