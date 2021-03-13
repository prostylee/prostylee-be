package vn.prostylee.location.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.location.entity.Address;

@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {

}
