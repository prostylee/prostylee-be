package vn.prostylee.core.repository.query;

import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HibernateQueryResult<R> {

	private final EntityManager em;
	private final Class<R> domainClass;
	private final StringBuilder queryBuilder;
	private final Pageable pageable;

	public HibernateQueryResult(@NonNull EntityManager em, @NonNull Class<R> domainClass, @NonNull StringBuilder queryBuilder) {
		this(em, domainClass, queryBuilder, null);
	}

	public HibernateQueryResult(@NonNull EntityManager em, @NonNull Class<R> domainClass, @NonNull StringBuilder queryBuilder, Pageable pageable) {
		this.em = em;
		this.domainClass = domainClass;
		this.queryBuilder = queryBuilder;
		if (pageable == null) {
			this.pageable = Pageable.unpaged();
		} else {
			this.pageable = pageable;
		}
	}

	public Optional<R> getSingleResult(Map<String, Object> filterParameterMap) {
		TypedQuery<R> query = em.createQuery(queryBuilder.toString(), domainClass);
		this.setParameters(query, filterParameterMap);
		try {
			return Optional.ofNullable(query.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public Page<R> getResultList(Map<String, Object> filterParameters, String... customCountQuery) {
		if (pageable.isUnpaged()) {
			return getAllUnPagedList(filterParameters);
		}
		return getAllPagedList(filterParameters, customCountQuery);
	}

	private Page<R> getAllUnPagedList(Map<String, Object> filterParameters) {
		appendSortQuery(pageable);

		TypedQuery<R> query = em.createQuery(queryBuilder.toString(), domainClass);
		this.setParameters(query, filterParameters);

		List<R> content = query.getResultList();
		return new PageImpl<>(content, pageable, content.size());
	}
	
	private Page<R> getAllPagedList(Map<String, Object> filterParameters, String... customCountQuery) {
		String countQueryStr = "SELECT COUNT(*) " + queryBuilder.substring(queryBuilder.indexOf("FROM")).replace("FETCH", "");
		if (ArrayUtils.isNotEmpty(customCountQuery)) {
			countQueryStr = customCountQuery[0];
		}
		TypedQuery<Long> countQuery = em.createQuery(countQueryStr, Long.class);
		this.setParameters(countQuery, filterParameters);
		long total = countQuery.getSingleResult();
		List<R> content = new ArrayList<>();
		if (total > 0) {
			appendSortQuery(pageable);

			TypedQuery<R> query = em.createQuery(queryBuilder.toString(), domainClass);
			this.setParameters(query, filterParameters);

			query.setFirstResult((int) pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());

			content = query.getResultList();
		}
		
		return new PageImpl<>(content, pageable, total);
	}

	private void setParameters(Query query, Map<String, Object> filterParameters) {
		filterParameters.forEach(query::setParameter);
	}

	private void appendSortQuery(Pageable pageable) {
		String sort = pageable.getSort().toString().replace(":", "");
        if (StringUtils.isNotBlank(sort) && !StringUtils.equalsIgnoreCase(Sort.unsorted().toString(), sort)) {
        	queryBuilder.append(" ORDER BY ").append(sort);
        }
	}
}
