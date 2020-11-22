package vn.prostylee.core.specs;

import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface CustomSearchable<T, P> {

    Specification<T> search(P param);
}
