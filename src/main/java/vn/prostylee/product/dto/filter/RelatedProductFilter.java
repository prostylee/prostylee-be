package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.PagingAndSortingParam;

@Data
@EqualsAndHashCode(callSuper = true)
public class RelatedProductFilter extends PagingAndSortingParam {

    private Boolean newest;

    private Boolean hot;

    @Override
    public String[] getSortableFields() {
        return new String[0];
    }
}
