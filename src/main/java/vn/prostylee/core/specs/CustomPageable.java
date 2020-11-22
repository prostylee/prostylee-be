package vn.prostylee.core.specs;

import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface CustomPageable<P> {

    Pageable page(P param);
}
