package vn.prostylee.auth.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.auth.entity.UserTemp;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository for domain model class AccountTemp.
 * @see UserTemp ;
 * @author prostylee
 */
public interface UserTempRepository extends BaseRepository<UserTemp, Long> {

    @Query("select e from #{#entityName} e where e.username=?1 AND e.expiredAt>=?2")
    Optional<List<UserTemp>> getUserTemps(String username, Date beforeExpiredDate);

    void deleteByUsername(String username);
}
