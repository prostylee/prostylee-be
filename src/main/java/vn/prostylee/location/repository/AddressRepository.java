package vn.prostylee.location.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.location.entity.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {

    Optional<Address> findByCode(String code);

    Optional<Address> findByCodeAndParentCode(String code, String parentCode);
}
