package vn.prostylee.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable>
        extends SoftDeleteRepository<T, ID>, JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}

