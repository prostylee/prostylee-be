package vn.prostylee.product.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.product.entity.Attribute;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryFilter extends BaseFilter {

    private String name;

    private Integer order;

    private Long parentId;

    private List<Attribute> attributes;

    @Override
    public String[] getSearchableFields() {
        return new String[] {
                "name",
                "order"
        };
    }

    @Override
    public String[] getSortableFields() {
        return new String[] {
                "name",
                "order"
        };
    }
}
