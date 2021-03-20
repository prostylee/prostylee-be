package vn.prostylee.store.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.store.entity.Store;

import java.util.List;

/**
 * Repository for domain model class Store.
 * @see Store;
 * @author prostylee
 */
@Repository
public interface StoreRepository extends BaseRepository<Store, Long> {


    @Query("SELECT e FROM #{#entityName} e where e.id in :ids")
    List<Store> findStoresByIds(@Param("ids") List<Long> ids);
}
