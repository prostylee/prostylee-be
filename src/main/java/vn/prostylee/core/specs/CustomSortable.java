package vn.prostylee.core.specs;


import org.springframework.data.domain.Sort;

@FunctionalInterface
public interface CustomSortable<P> {

    Sort sort(P param);
}
