package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.dto.filter.NearestLocationFilter;
import vn.prostylee.location.dto.response.LocationResponse;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.store.constants.StoreStatus;
import vn.prostylee.store.converter.StoreConverter;
import vn.prostylee.store.dto.filter.MostActiveStoreFilter;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.filter.StoreProductFilter;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreMiniResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;
import vn.prostylee.store.entity.Company;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.repository.StoreRepository;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.service.UserMostActiveService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final BaseFilterSpecs<Store> baseFilterSpecs;

    private final AuthenticatedProvider authenticatedProvider;

    private final LocationService locationService;

    private final UserMostActiveService userMostActiveService;

    private final StoreConverter storeConverter;

    private final AttachmentService attachmentService;

    @Override
    public Page<StoreResponse> findAll(BaseFilter baseFilter) {
        StoreFilter storeFilter = (StoreFilter) baseFilter;
        Specification<Store> searchableSpec = buildSearchable(storeFilter);

        Map<Long, LocationResponse> mapNearByLocations = null;
        boolean isSearchNearBy = storeFilter.getLatitude() != null && storeFilter.getLongitude() != null;
        if (isSearchNearBy) {
            List<LocationResponse> locations = getLocationsNearBy(storeFilter);
            if (CollectionUtils.isNotEmpty(locations)) {
                mapNearByLocations = locations.stream()
                        .collect(Collectors.toMap(LocationResponse::getId, location -> location));
                searchableSpec = buildNearBySpec(searchableSpec, mapNearByLocations.keySet());
            }
        }

        Pageable pageable = baseFilterSpecs.page(storeFilter);
        Page<Store> page = storeRepository.findAllActive(searchableSpec, pageable);

        final Map<Long, LocationResponse> nearByLocations = Optional.ofNullable(mapNearByLocations).orElseGet(HashMap::new);
        List<StoreResponse> responses = page.getContent()
                .stream()
                .map(store -> {
                    StoreResponse response = storeConverter.convertToFullResponse(store, storeFilter.getNumberOfProducts());
                    Optional.ofNullable(response.getLocationId())
                            .ifPresent(locationId -> response.setLocation(nearByLocations.getOrDefault(locationId, null)));
                    return storeConverter.convertToFullResponse(store, response, storeFilter.getNumberOfProducts());
                })
                .collect(Collectors.toList());

        if (isSearchNearBy) {
            responses.sort(storeResponseComparator());
        }

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    private Comparator<StoreResponse> storeResponseComparator() {
        return (store1, store2) -> {
            if (store1.getLocation() == null) {
                return 1;
            }
            return store1.getLocation().compareTo(store2.getLocation());
        };
    }

    @Cacheable(cacheNames = CachingKey.STORES, key = "#id")
    @Override
    public StoreResponse findById(Long id) {
        Store store = getById(id);
        return storeConverter.convertToFullResponse(store, 0);
    }

    @Override
    public Page<StoreMiniResponse> getMiniStoreResponse(StoreProductFilter storeFilter) {
        Page<StoreResponse> storeResponses = findAll(storeFilter);
        return storeResponses.map(storeConverter::convertToMiniResponse);
    }

    @Override
    public List<Long> getNewStoreIds(NewestStoreRequest request) {
        Pageable pageSpecification = PageRequest.of(request.getPage(), request.getLimit());
        return storeRepository.findNewestStoreIds(request.getFromDate(), request.getToDate(), pageSpecification);
    }

    @Override
    public StoreResponse save(StoreRequest storeRequest) {
        Company company = new Company();
        company.setId(storeRequest.getCompanyId());

        Store store = BeanUtil.copyProperties(storeRequest, Store.class);
        store.setOwnerId(authenticatedProvider.getUserIdValue());
        store.setCompany(company);
        store.setLogo(Optional.ofNullable(attachmentService.save(storeRequest.getLogoImage()))
                .map(Attachment::getId).orElse(0L));

        if (storeRequest.getStatus() == null) {
            store.setStatus(StoreStatus.IN_PROGRESS.getValue());
        }

        Store savedStore = storeRepository.save(store);

        return storeConverter.convertToResponse(savedStore);
    }

    @CachePut(cacheNames = CachingKey.STORES, key = "#id")
    @Override
    public StoreResponse update(Long id, StoreRequest storeRequest) {
        Company company = new Company();
        company.setId(storeRequest.getCompanyId());

        Store store = getById(id);
        BeanUtil.mergeProperties(storeRequest, store);
        store.setCompany(company);

        Store savedStore = storeRepository.save(store);
        return storeConverter.convertToResponse(savedStore);
    }

    @CacheEvict(cacheNames = CachingKey.STORES, allEntries = true)
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

    @Override
    public Page<StoreResponse> getTopProductsOfStores(MostActiveStoreFilter storeFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.STORE.name()))
                .fromDate(DateUtils.getLastDaysBefore(storeFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build()
                .pagingParam(new PagingParam(storeFilter.getPage(), storeFilter.getLimit()));

        List<Long> storeIds = userMostActiveService.getTargetIdsByMostActive(request);
        List<Store> stores;
        if (CollectionUtils.isEmpty(storeIds)) {
            Pageable pageSpecification = PageRequest.of(0, request.getLimit());
            stores = storeRepository.findAllActive(pageSpecification).getContent();
        } else {
            stores = storeRepository.findStoresByIds(storeIds);
        }
        return new PageImpl<>(getMostActiveStoresByStoreIds(stores, storeFilter.getNumberOfProducts()));
    }

    private Specification<Store> buildSearchable(StoreFilter storeFilter) {
        Specification<Store> spec = baseFilterSpecs.search(storeFilter);

        if (storeFilter.getStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), storeFilter.getStatus()));
        } else {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), StoreStatus.ACTIVE.getValue()));
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

    private Specification<Store> buildNearBySpec(Specification<Store> spec, Set<Long> locationIds) {
        spec = spec.and((root, query, cb) -> {
            CriteriaBuilder.In<Long> inClause = cb.in(root.get("locationId"));
            locationIds.forEach(inClause::value);
            return inClause;
        });
        return spec;
    }

    private List<LocationResponse> getLocationsNearBy(StoreFilter storeFilter) {
        NearestLocationFilter locationFilter = new NearestLocationFilter();
        locationFilter.setLatitude(storeFilter.getLatitude());
        locationFilter.setLongitude(storeFilter.getLongitude());
        locationFilter.setLimit(storeFilter.getLimit());
        locationFilter.setPage(storeFilter.getPage());
        locationFilter.setTargetType(TargetType.STORE.name());

        return locationService.getNearestLocations(locationFilter);
    }

    @Override
    public Store getById(Long id) {
        return storeRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store is not found with id [" + id + "]"));
    }

    private List<StoreResponse> getMostActiveStoresByStoreIds(List<Store> stores, int numberOfProducts) {
        return stores.stream()
                .map(store -> storeConverter.convertToFullResponse(store, numberOfProducts))
                .collect(Collectors.toList());
    }

    @Override
    public StoreResponseLite getStoreResponseLite(Long id) {
        return fetchById(id)
                .map(storeResponse -> BeanUtil.copyProperties(storeResponse, StoreResponseLite.class))
                .orElse(null);
    }

}
