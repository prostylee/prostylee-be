package vn.prostylee.core.dto.filter;

import lombok.Data;

/**
 * The base class for search with keyword, paging and sorting
 */
@Data
public abstract class BaseFilter extends PagingAndSortingParam implements SearchingDefinitions {

    private String keyword;

}
