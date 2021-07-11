package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.LatLngDto;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.AddressService;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.store.dto.filter.BranchFilter;
import vn.prostylee.store.dto.request.BranchRequest;
import vn.prostylee.store.dto.response.CitiesWithBranchesResponse;
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.entity.Branch;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.repository.BranchRepository;
import vn.prostylee.store.service.BranchService;

import javax.persistence.criteria.Join;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    private final BaseFilterSpecs<Branch> baseFilterSpecs;

    private final LocationService locationService;

    private final AddressService addressService;

    @Override
    public Page<BranchResponse> findAll(BaseFilter baseFilter) {
        BranchFilter branchFilter = (BranchFilter) baseFilter;
        Specification<Branch> searchableSpec = buildSearchable(branchFilter);


        Pageable pageable = baseFilterSpecs.page(branchFilter);
        Page<Branch> page = branchRepository.findAllActive(searchableSpec, pageable);

        List<BranchResponse> branches;
        boolean isSearchNearBy = branchFilter.getLatitude() != null && branchFilter.getLongitude() != null;
        if (isSearchNearBy) {
            LatLngDto toLatLngDto = LatLngDto.builder()
                    .latitude(branchFilter.getLatitude())
                    .longitude(branchFilter.getLongitude())
                    .build();

            branches = page.getContent()
                    .stream()
                    .map(branch -> {
                        BranchResponse response = convert(branch);
                        response.setLocation(getLocationByIdWithDistanceCalculation(response.getLocationId(), toLatLngDto));
                        return response;
                    })
                    .sorted(branchResponseComparator())
                    .collect(Collectors.toList());
        } else {
            branches = page.getContent()
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(branches, pageable, page.getTotalElements());
    }

    @Override
    public List<CitiesWithBranchesResponse> getListCities(Long storeId){
        List<String> cities = branchRepository.geCitiesByStoreId(storeId);
        List<CitiesWithBranchesResponse> responses = cities.stream()
                .map(city -> {
                    CitiesWithBranchesResponse response = new CitiesWithBranchesResponse();
                    response.setCityCode(city);
                    AddressResponse cityObject = this.addressService.findByCode(city);
                    response.setCityName(cityObject.getName());
                    return response;
                }).collect(Collectors.toList());
        return responses;
    }

    private Comparator<BranchResponse> branchResponseComparator() {
        return (branch1, branch2) -> {
            if (branch1.getLocation() == null) {
                return 1;
            }
            return branch1.getLocation().compareTo(branch2.getLocation());
        };
    }

    private Specification<Branch> buildSearchable(BranchFilter branchFilter) {
        Specification<Branch> spec = baseFilterSpecs.search(branchFilter);

        if (BooleanUtils.isTrue(branchFilter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));
        } else if (BooleanUtils.isFalse(branchFilter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), false));
        }

        if (branchFilter.getStoreId() != null) {
            Specification<Branch> joinCompanySpec = (root, query, cb) -> {
                Join<Branch, Store> company = root.join("store");
                return cb.equal(company.get("id"), branchFilter.getStoreId());
            };
            spec = spec.and(joinCompanySpec);
        }

        if(branchFilter.getCityCode() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("cityCode"), branchFilter.getCityCode()));
        }

        return spec;
    }

    private BranchResponse convert(Branch branch) {
        return BeanUtil.copyProperties(branch, BranchResponse.class);
    }

    @Cacheable(cacheNames = CachingKey.BRANCHES, key = "#id")
    @Override
    public BranchResponse findById(Long id) {
        Branch branch = getById(id);
        return convert(branch);
    }

    @Override
    public BranchResponse save(BranchRequest branchRequest) {
        Store store = new Store();
        store.setId(branchRequest.getStoreId());

        AddressResponse city = this.addressService.findByCode(branchRequest.getCityCode());
        AddressResponse district = this.addressService.findByCodeAndParentCode(branchRequest.getDistrictCode(), city.getCode());
        AddressResponse ward = this.addressService.findByCodeAndParentCode(branchRequest.getWardCode(), district.getCode());
        String fullAddress = String.format("%s, %s, %s", branchRequest.getAddress(), ward.getName(), district.getName());

        Branch branch = BeanUtil.copyProperties(branchRequest, Branch.class);
        branch.setStore(store);
        branch.setFullAddress(fullAddress);

        if (branchRequest.getActive() == null) {
            branch.setActive(true);
        }

        Branch savedBranch = branchRepository.save(branch);

        return convert(savedBranch);
    }

    @CachePut(cacheNames = CachingKey.BRANCHES, key = "#id")
    @Override
    public BranchResponse update(Long id, BranchRequest branchRequest) {
        Store store = new Store();
        store.setId(branchRequest.getStoreId());

        Branch branch = getById(id);
        BeanUtil.mergeProperties(branchRequest, branch);
        branch.setStore(store);

        Branch savedBranch = branchRepository.save(branch);
        return convert(savedBranch);
    }

    @CacheEvict(cacheNames = CachingKey.BRANCHES, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        try {
            return branchRepository.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a branch without existing in database", e);
            return false;
        }
    }

    @Override
    public Branch getById(Long id) {
        return branchRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch is not found with id [" + id + "]"));
    }

    private LocationResponse getLocationByIdWithDistanceCalculation(Long locationId, LatLngDto toLatLngDto) {
        return Optional.ofNullable(locationId)
                .map(id -> locationService.getLocationByIdWithDistanceCalculation(id, toLatLngDto))
                .orElse(null);
    }
}
