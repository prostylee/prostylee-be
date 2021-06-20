package vn.prostylee.location.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.location.entity.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {

    Optional<Address> findByCode(String code);

    Optional<Address> findByCodeAndParentCode(String code, String parentCode);

    @Query("SELECT e FROM #{#entityName} e where e.id in :ids")
    List<Address> findByIds(@Param("ids") List<Long> ids);
}
