package vn.prostylee.core.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;

/**
 * The base class for search with keyword, paging and sorting
 */
@Valid
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseFilter extends PagingAndSortingParam implements SearchingDefinitions {

    private String keyword;

}
