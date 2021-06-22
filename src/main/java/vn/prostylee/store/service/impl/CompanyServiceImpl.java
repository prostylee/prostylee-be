package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.store.dto.filter.CompanyFilter;
import vn.prostylee.store.dto.request.CompanyRequest;
import vn.prostylee.store.dto.response.CompanyResponse;
import vn.prostylee.store.entity.Company;
import vn.prostylee.store.repository.CompanyRepository;
import vn.prostylee.store.service.CompanyService;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final BaseFilterSpecs<Company> baseFilterSpecs;

    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public Page<CompanyResponse> findAll(BaseFilter baseFilter) {
        CompanyFilter companyFilter = (CompanyFilter) baseFilter;
        Specification<Company> spec = baseFilterSpecs.search(baseFilter);
        Pageable pageable = baseFilterSpecs.page(companyFilter);
        Page<Company> page = companyRepository.findAllActive(spec, pageable);
        return page.map(this::convertToResponse);
    }

    private CompanyResponse convertToResponse(Company company) {
        return BeanUtil.copyProperties(company, CompanyResponse.class);
    }

    @Cacheable(value = CachingKey.COMPANIES, key = "#id")
    @Override
    public CompanyResponse findById(Long id) {
        Company company = getById(id);
        return convertToResponse(company);
    }

    private Company getById(Long id) {
        return companyRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company is not found with id [" + id + "]"));
    }

    @Override
    public CompanyResponse save(CompanyRequest companyRequest) {
        Company company = BeanUtil.copyProperties(companyRequest, Company.class);
        company.setOwnerId(authenticatedProvider.getUserIdValue());
        if (companyRequest.getActive() == null) {
            company.setActive(Boolean.TRUE);
        }
        Company savedCompany = companyRepository.save(company);
        return convertToResponse(savedCompany);
    }

    @CachePut(cacheNames = CachingKey.COMPANIES, key = "#id")
    @Override
    public CompanyResponse update(Long id, CompanyRequest companyRequest) {
        Company company = getById(id);
        BeanUtil.mergeProperties(companyRequest, company);
        Company savedCompany = companyRepository.save(company);
        return convertToResponse(savedCompany);
    }

    @CacheEvict(value = CachingKey.COMPANIES, key = "#id")
    @Override
    public boolean deleteById(Long id) {
        try {
            return companyRepository.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a company without existing in database", e);
            return false;
        }
    }
}
