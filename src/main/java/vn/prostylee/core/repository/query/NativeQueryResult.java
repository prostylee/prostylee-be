package vn.prostylee.core.repository.query;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NativeQueryResult<R> {

	private final EntityManager em;
	private final Class<R> domainClass;
	private final StringBuilder queryBuilder;
	private final Pageable pageable;

	public NativeQueryResult(@NonNull EntityManager em, @NonNull Class<R> domainClass, @NonNull StringBuilder queryBuilder) {
		this(em, domainClass, queryBuilder, null);
	}

	public NativeQueryResult(@NonNull EntityManager em, @NonNull Class<R> domainClass, @NonNull StringBuilder queryBuilder, Pageable pageable) {
		this.em = em;
		this.domainClass = domainClass;
		this.queryBuilder = queryBuilder;
		if (pageable == null) {
			this.pageable = Pageable.unpaged();
		} else {
			this.pageable = pageable;
		}
	}

	public Optional<R> getSingleResult(Map<String, Object> filterParameters) {
		Query query = em.createNativeQuery(queryBuilder.toString());
		this.setParameters(query, filterParameters);
		NativeQuery<R> sqlQuery = query.unwrap(NativeQuery.class);
		try {
			return Optional.ofNullable(
					sqlQuery.setResultTransformer(new SimpleResultTransformer(domainClass))
							.getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	public Page<R> getResultList(Map<String, Object> filterParameters) {
		if (pageable.isUnpaged()) {
			return getAllUnPagedList(filterParameters);
		}
		return getAllPagedList(filterParameters);
	}

	private Page<R> getAllUnPagedList(Map<String, Object> filterParameters) {
		String sort = pageable.getSort().toString().replace(":", "");
		appendSortQuery(sort);

		Query query = em.createNativeQuery(queryBuilder.toString());
		this.setParameters(query, filterParameters);
		NativeQuery<R> sqlQuery = query.unwrap(NativeQuery.class);

		List<R> content = sqlQuery.setResultTransformer(new SimpleResultTransformer(domainClass)).list();
	    return new PageImpl<>(content, pageable, content.size());
	}

	private Page<R> getAllPagedList(Map<String, Object> filterParameters) {
		Query query = em.createNativeQuery("SELECT COUNT(*) FROM ( " + queryBuilder.toString() + " ) dataTable");
		this.setParameters(query, filterParameters);
		long total = Long.parseLong(query.getSingleResult().toString());
		List<R> content = new ArrayList<>();
		if (total > 0) {
			String sort = pageable.getSort().toString().replace(":", "");
			appendSortQuery(sort);

			query = em.createNativeQuery(queryBuilder.toString());
			this.setParameters(query, filterParameters);
			query.setFirstResult(Integer.parseInt(String.valueOf(pageable.getOffset())));
			query.setMaxResults(pageable.getPageSize());

			NativeQuery<R> sqlQuery = query.unwrap(NativeQuery.class);
			content = sqlQuery.setResultTransformer(new SimpleResultTransformer(domainClass)).list();
		}
	    return new PageImpl<>(content, pageable, total);
	}

	private void setParameters(Query query, Map<String, Object> filterParameters) {
		filterParameters.forEach(query::setParameter);
	}

	private void appendSortQuery(String sort) {
		if (StringUtils.isBlank(sort) || Sort.unsorted().toString().equals(sort)) {
			queryBuilder.append(" ORDER BY createdAt DESC");
		} else {
			queryBuilder.append(" ORDER BY ").append(sort);
		}
	}
}
