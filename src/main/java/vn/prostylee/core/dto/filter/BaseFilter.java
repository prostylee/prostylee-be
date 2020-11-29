package vn.prostylee.core.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The base class for search with keyword, paging and sorting
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseFilter extends PagingAndSortingParam implements SearchingDefinitions {

    private String keyword;

}
