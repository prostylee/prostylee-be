package vn.prostylee.auth.repository.custom;

import vn.prostylee.auth.dto.filter.AccountFilter;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.auth.repository.AccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomAccountRepository extends AccountRepository {

    Optional<Account> findByActivatedUsername(String username);

    Optional<Account> findActivatedUserByEmail(String email);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByPushToken(String pushToken);

    Page<Account> getAllAccounts(AccountFilter baseFilter, Pageable pageable);
}
