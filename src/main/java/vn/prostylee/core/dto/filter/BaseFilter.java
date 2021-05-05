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
public class BaseFilter extends PagingAndSortingParam implements SearchingDefinitions {

    private String keyword;

    @Override
    public String[] getSearchableFields() {
        return new String[0];
    }

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
