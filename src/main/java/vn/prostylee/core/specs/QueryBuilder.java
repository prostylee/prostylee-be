package vn.prostylee.core.specs;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T> {

    private CriteriaBuilder cb;

    private Root<T> root;

    private List<Predicate> predicates = new ArrayList<>();

    public QueryBuilder(CriteriaBuilder cb, Root<T> root) {
        this.cb = cb;
        this.root = root;
    }

    public QueryBuilder likeIgnoreCase(String propertyPath, String value) {
        if (StringUtils.isNotBlank(value)) {
            predicates.add(cb.like(cb.upper(root.get(propertyPath).as(String.class)), "%" + StringUtils.upperCase(value) + "%"));
        }
        return this;
    }

    public QueryBuilder likeIgnoreCaseRef(String refEntity, String refField, String value, JoinType joinType) {
        if (StringUtils.isNotBlank(value)) {
            predicates.add(cb.like(cb.upper(root.join(refEntity, joinType).get(refField).as(String.class)),"%" + StringUtils.upperCase(value) + "%"));
        }
        return this;
    }

    public QueryBuilder equalsIgnoreCase(String propertyPath, String value) {
        if (StringUtils.isNotBlank(value)) {
            predicates.add(cb.equal(cb.upper(root.get(propertyPath).as(String.class)), StringUtils.upperCase(value)));
        }
        return this;
    }

    public QueryBuilder equals(String propertyPath, Object value) {
        if (value != null && StringUtils.isNotBlank(value.toString())) {
            predicates.add(cb.equal(root.get(propertyPath), value));
        }
        return this;
    }

    public QueryBuilder greaterThanOrEqualTo(String propertyPath, Double value) {
        if (value != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(propertyPath), value));
        }
        return this;
    }

    public QueryBuilder lessThanOrEqualTo(String propertyPath, Double value) {
        if (value != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(propertyPath), value));
        }
        return this;
    }

    public Predicate[] build() {
        return predicates.toArray(new Predicate[0]);
    }
}