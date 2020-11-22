package vn.prostylee.auth.repository.impl;

import vn.prostylee.auth.dto.filter.AccountFilter;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.core.repository.query.HibernateQueryResult;
import vn.prostylee.core.repository.impl.BaseRepositoryImpl;
import vn.prostylee.auth.repository.custom.CustomAccountRepository;
import vn.prostylee.core.utils.DbUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Qualifier("customAccountRepository")
public class AccountRepositoryImpl extends BaseRepositoryImpl<Account, Long> implements CustomAccountRepository {

    public AccountRepositoryImpl(EntityManager em) {
        super(Account.class, em);
    }

    @Override
    public Optional<Account> findByActivatedUsername(String username) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ Account.class.getName() +" e "
                + " where e.username= :username "
                + " AND e.active= true "
                + " AND e.deletedAt is null");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("username", username);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, Account.class);
    }

    @Override
    public Optional<Account> findActivatedUserByEmail(String email) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ Account.class.getName() +" e "
                + " where e.email= :email "
                + " AND e.active= true "
                + " AND e.deletedAt is null");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("email", email);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, Account.class);
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ Account.class.getName() +" e "
                + " where e.username= :username ");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("username", username);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, Account.class);
    }

    @Override
    public Optional<Account> findByPushToken(String pushToken) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ Account.class.getName() +" e "
                + " where e.pushToken= :pushToken ");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("pushToken", pushToken);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, Account.class);
    }

    @Override
    public Page<Account> getAllAccounts(AccountFilter filter, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder("SELECT account "
                + " FROM "+ Account.class.getName() + " account "
                + " JOIN account.roles as roles "
                + " WHERE account.deletedAt = null ");

        Map<String, Object> parameterMap = buildFilterParameters(queryBuilder, filter);
        queryBuilder.append(" GROUP BY account.id ");
        appendSortQuery(queryBuilder, pageable);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, pageable);
        return queryResult.getResultList(parameterMap, Account.class);
    }

    private void appendSortQuery(StringBuilder queryBuilder, Pageable pageable) {
        // Append order by
        String sort = pageable.getSort().toString().replace(":", "");
        if (StringUtils.isNotBlank(sort) && !StringUtils.equals(Sort.unsorted().toString(), sort)) {
            queryBuilder.append(" ORDER BY account.").append(sort);
        }
    }

    private static Map<String, Object> buildFilterParameters(StringBuilder queryBuilder, AccountFilter filter) {
        Map<String, Object> parameterMap = new HashMap<>();
        if(null == filter) {
            return parameterMap;
        }

        if (StringUtils.isNotBlank(filter.getKeyword())) {
            queryBuilder.append(" AND (account.fullName LIKE :keyword "
                    + " OR account.phoneNumber LIKE :keyword "
                    + " OR account.email LIKE :keyword"
                    + " OR account.username LIKE :keyword)"
            );
            parameterMap.put("keyword", DbUtil.getSearchLikeQueryValue(filter.getKeyword()));
            return parameterMap;
        }

        if (StringUtils.isNotBlank(filter.getUsername())) {
            queryBuilder.append(" AND account.username LIKE :username");
            parameterMap.put("username", DbUtil.getSearchLikeQueryValue(filter.getUsername()));
        }

        if (StringUtils.isNotBlank(filter.getEmail())) {
            queryBuilder.append(" AND account.email LIKE :email");
            parameterMap.put("email", DbUtil.getSearchLikeQueryValue(filter.getEmail()));
        }

        if (CollectionUtils.isNotEmpty(filter.getRoleCodes())) {
            queryBuilder.append(" AND roles.code IN (:roleCodes)");
            parameterMap.put("roleCodes", filter.getRoleCodes());
        }
        return parameterMap;
    }
}
