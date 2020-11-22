package vn.prostylee.core.repository.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HibernateQueryResult {

	private EntityManager em;
	private StringBuilder queryBuilder;
	private Pageable pageable;
	/** When request send page size = 1, ignore paging **/
	private static final int UNPAGED_SIZE = 1;
	
	public HibernateQueryResult(EntityManager em, StringBuilder queryBuilder, Pageable pageable) {
		super();
		this.em = em;
		this.queryBuilder = queryBuilder;
		this.pageable = pageable;
	}

	public <K> Page<K> getResultList(Map<String, Object> filterParameters, Class<K> responseClass) {
		Page<K> resulstList;
		if (pageable.getPageSize() == UNPAGED_SIZE) {
			resulstList = getAllUnPagedList(filterParameters, responseClass);
		} else {
			resulstList = getAllPagedList(filterParameters, responseClass);
		}
	    return resulstList;
	}

	private <K> Page<K> getAllUnPagedList(Map<String, Object> filterParameters, Class<K> responseClass) {
		Query query = em.createQuery(queryBuilder.toString(), responseClass);
		this.setParameters(query, filterParameters);
		List<K> resultList = query.getResultList();
		return new PageImpl<>(resultList, pageable, resultList.size());
		
	}
	
	private <K> Page<K> getAllPagedList(Map<String, Object> filterParameters, Class<K> responseClass) {
		//Query countQuery = em.createQuery("SELECT COUNT(*) FROM ( " + queryBuilder.toString().replaceAll("FETCH", "") + " ) dataTable");
		Query countQuery = em.createQuery(queryBuilder.toString(), responseClass);
		this.setParameters(countQuery, filterParameters);
		long total = countQuery.getResultList().size();
		List<K> resultList = new ArrayList<>();
		if (total > 0) {
			Query query = em.createQuery(queryBuilder.toString(), responseClass);
			this.setParameters(query, filterParameters);
			query.setFirstResult((int) pageable.getOffset());
			query.setMaxResults(pageable.getPageSize());
			resultList = query.getResultList();
		}
		
		return new PageImpl<>(resultList, pageable, total);
	}
	
	public <K> Optional<K> getQuerySingleResult(Map<String, Object> filterparameterMap, Class<K> responseClass) {
		Query query = em.createQuery(queryBuilder.toString(), responseClass);
		this.setParameters(query, filterparameterMap);
		K result;
		try {
			result = (K) query.getSingleResult();
		} catch (NoResultException e) {
			return Optional.empty();
		}
	    return Optional.of(result);
	}

	private void setParameters(Query query, Map<String, Object> filterParameters) {
		filterParameters.forEach(query::setParameter);

	}
}
