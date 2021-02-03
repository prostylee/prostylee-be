package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.CompanyResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.entity.Company;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.repository.StoreRepository;
import vn.prostylee.store.service.StoreService;

import javax.persistence.criteria.Join;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final BaseFilterSpecs<Store> baseFilterSpecs;

    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public Page<StoreResponse> findAll(BaseFilter baseFilter) {
        StoreFilter storeFilter = (StoreFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(storeFilter);
        Page<Store> page = storeRepository.findAllActive(buildSearchable(storeFilter), pageable);
        return page.map(this::convertToResponse);
    }

    private Specification<Store> buildSearchable(StoreFilter storeFilter) {
        Specification<Store> spec = baseFilterSpecs.search(storeFilter);

        if (storeFilter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), storeFilter.getStatus()));
        }

        if (storeFilter.getOwnerId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ownerId"), storeFilter.getOwnerId()));
        }

        if (storeFilter.getCompanyId() != null) {
            Specification<Store> joinCompanySpec = (root, query, cb) -> {
                Join<Store, Company> company = root.join("company");
                return cb.equal(company.get("id"), storeFilter.getCompanyId());
            };
            spec = spec.and(joinCompanySpec);
        }

        return spec;
    }

    private StoreResponse convertToResponse(Store store) {
        StoreResponse storeResponse = BeanUtil.copyProperties(store, StoreResponse.class);
        storeResponse.setCompany(BeanUtil.copyProperties(store.getCompany(), CompanyResponse.class));
        return storeResponse;
    }

    @Override
    public StoreResponse findById(Long id) {
        Store store = getById(id);
        return convertToResponse(store);
    }

    private Store getById(Long id) {
        return storeRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store is not found with id [" + id + "]"));
    }

    @Override
    public StoreResponse save(StoreRequest storeRequest) {
        Company company = new Company();
        company.setId(storeRequest.getCompanyId());

        Store store = BeanUtil.copyProperties(storeRequest, Store.class);
        store.setOwnerId(authenticatedProvider.getUserIdValue());
        store.setCompany(company);
        Store savedStore = storeRepository.save(store);
        return convertToResponse(savedStore);
    }

    @Override
    public StoreResponse update(Long id, StoreRequest storeRequest) {
        Company company = new Company();
        company.setId(storeRequest.getCompanyId());

        Store store = getById(id);
        BeanUtil.mergeProperties(storeRequest, store);
        store.setCompany(company);
        Store savedStore = storeRepository.save(store);
        return convertToResponse(savedStore);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            storeRepository.softDelete(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a store without existing in database", e);
            return false;
        }
    }
}
