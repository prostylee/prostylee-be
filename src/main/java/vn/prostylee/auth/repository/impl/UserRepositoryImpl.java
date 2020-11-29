package vn.prostylee.auth.repository.impl;

import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.repository.query.HibernateQueryResult;
import vn.prostylee.core.repository.impl.BaseRepositoryImpl;
import vn.prostylee.auth.repository.custom.CustomUserRepository;
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
@Qualifier("customUserRepository")
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements CustomUserRepository {

    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public Optional<User> findByActivatedUsername(String username) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ User.class.getName() +" e "
                + " where e.username= :username "
                + " AND e.active= true "
                + " AND e.deletedAt is null");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("username", username);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, User.class);
    }

    @Override
    public Optional<User> findActivatedUserByEmail(String email) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ User.class.getName() +" e "
                + " where e.email= :email "
                + " AND e.active= true "
                + " AND e.deletedAt is null");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("email", email);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ User.class.getName() +" e "
                + " where e.username= :username ");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("username", username);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, User.class);
    }

    @Override
    public Optional<User> findByPushToken(String pushToken) {
        StringBuilder queryBuilder = new StringBuilder("select e "
                + " from "+ User.class.getName() +" e "
                + " where e.pushToken= :pushToken ");

        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("pushToken", pushToken);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, null);
        return queryResult.getQuerySingleResult(parameterMap, User.class);
    }

    @Override
    public Page<User> getAllUsers(UserFilter filter, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder("SELECT user "
                + " FROM "+ User.class.getName() + " user "
                + " JOIN user.roles as roles "
                + " WHERE user.deletedAt = null ");

        Map<String, Object> parameterMap = buildFilterParameters(queryBuilder, filter);
        queryBuilder.append(" GROUP BY user.id ");
        appendSortQuery(queryBuilder, pageable);
        HibernateQueryResult queryResult = new HibernateQueryResult(getEntityManager(), queryBuilder, pageable);
        return queryResult.getResultList(parameterMap, User.class);
    }

    private void appendSortQuery(StringBuilder queryBuilder, Pageable pageable) {
        // Append order by
        String sort = pageable.getSort().toString().replace(":", "");
        if (StringUtils.isNotBlank(sort) && !StringUtils.equals(Sort.unsorted().toString(), sort)) {
            queryBuilder.append(" ORDER BY user.").append(sort);
        }
    }

    private static Map<String, Object> buildFilterParameters(StringBuilder queryBuilder, UserFilter filter) {
        Map<String, Object> parameterMap = new HashMap<>();
        if(null == filter) {
            return parameterMap;
        }

        if (StringUtils.isNotBlank(filter.getKeyword())) {
            queryBuilder.append(" AND (user.fullName LIKE :keyword "
                    + " OR user.phoneNumber LIKE :keyword "
                    + " OR user.email LIKE :keyword"
                    + " OR user.username LIKE :keyword)"
            );
            parameterMap.put("keyword", DbUtil.getSearchLikeQueryValue(filter.getKeyword()));
            return parameterMap;
        }

        if (StringUtils.isNotBlank(filter.getUsername())) {
            queryBuilder.append(" AND user.username LIKE :username");
            parameterMap.put("username", DbUtil.getSearchLikeQueryValue(filter.getUsername()));
        }

        if (StringUtils.isNotBlank(filter.getEmail())) {
            queryBuilder.append(" AND user.email LIKE :email");
            parameterMap.put("email", DbUtil.getSearchLikeQueryValue(filter.getEmail()));
        }

        if (CollectionUtils.isNotEmpty(filter.getRoleCodes())) {
            queryBuilder.append(" AND roles.code IN (:roleCodes)");
            parameterMap.put("roleCodes", filter.getRoleCodes());
        }
        return parameterMap;
    }
}
