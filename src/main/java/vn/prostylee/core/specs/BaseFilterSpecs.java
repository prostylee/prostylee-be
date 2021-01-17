package vn.prostylee.core.specs;

import vn.prostylee.core.constant.ApiParamConstant;
import vn.prostylee.core.dto.filter.BaseFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BaseFilterSpecs<T> implements CustomSearchable<T, BaseFilter>,
        CustomSortable<BaseFilter>, CustomPageable<BaseFilter> {

    private static final String SORT_ASC = "+";

    private static final String SORT_DESC = "-";

    @Override
    public Specification<T> search(BaseFilter filterable) {
        return buildSpecification(filterable);
    }

    @Override
    public Sort sort(BaseFilter filterable) {
        return buildMultipleSorts(filterable);
    }

    @Override
    public Pageable page(BaseFilter filterable) {
    	if (filterable.getLimit() == ApiParamConstant.UNPAGED_LIMIT_VALUE) {
    		return Pageable.unpaged();
    	}
        return PageRequest.of(filterable.getPage(), filterable.getLimit(), sort(filterable));
    }

    private Sort buildMultipleSorts(BaseFilter filterable) {
        if (ArrayUtils.isEmpty(filterable.getSorts())){
            return Sort.unsorted();
        }

        List<Sort> sorts = Arrays.stream(filterable.getSorts()).map(param -> buildSorts(filterable.getSortableFields(), param))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (sorts.isEmpty()) {
            return Sort.unsorted();
        }
        Sort sort = sorts.get(0);
        for (int i = 1; i < sorts.size(); i++) {
            sort = sort.and(sorts.get(i));
        }
        return sort;
    }

    @Nullable
    private Sort buildSorts(String[] sortableFields, String param) {
        if(StringUtils.isBlank(param)) {
            return null;
        }
        param = param.trim();
        if (param.startsWith(SORT_DESC)) {
            String columnName = param.substring(1);
            if (isAllowedSortByField(sortableFields, columnName)) {
                return Sort.by(columnName).descending();
            }
        } else {
            String columnName = param;
            if (param.startsWith(SORT_ASC)) {
                columnName = param.substring(1);
            }
            if (isAllowedSortByField(sortableFields, columnName)) {
                return Sort.by(columnName).ascending();
            }
        }
        return null;
    }

    private boolean isAllowedSortByField(String[] sortableFields, String columnName) {
        return StringUtils.equalsAnyIgnoreCase(columnName, sortableFields);
    }

    private Specification<T> buildSpecification(BaseFilter filterable) {
        return (root, query, cb) -> {
            QueryBuilder queryBuilder = new QueryBuilder<>(cb, root);
            if (StringUtils.isNotBlank(filterable.getKeyword())) {
                String[] searchableFields = filterable.getSearchableFields();
                for(String searchByField : searchableFields) {
                    queryBuilder = queryBuilder.likeIgnoreCase(searchByField, filterable.getKeyword());
                }
            }
            Predicate[] orPredicates = queryBuilder.build();
            if (orPredicates.length > 0) {
                return cb.or(orPredicates);
            }
            return null;
        };
    }
}