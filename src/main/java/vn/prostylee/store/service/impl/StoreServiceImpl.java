package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.filter.MostActiveStoreFilter;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.filter.StoreProductFilter;
import vn.prostylee.store.dto.request.NewestStoreRequest;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.CompanyResponse;
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

    private final FileUploadService fileUploadService;

    private final LocationService locationService;

    private final UserMostActiveService userMostActiveService;

    private ProductService productService;

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
                    StoreResponse response = convertToResponse(store);
                    Optional.ofNullable(response.getLocationId())
                            .ifPresent(locationId -> response.setLocation(nearByLocations.getOrDefault(locationId, null)));
                    return convertToFullResponse(store, response, storeFilter.getNumberOfProducts());
                })
                .collect(Collectors.toList());

        if (isSearchNearBy) {
            responses.sort((store1, store2) -> {
                if (store1.getLocation() == null) {
                    return 1;
                }
                return store1.getLocation().compareTo(store2.getLocation());
            });
        }

        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    @Override
    public StoreResponse findById(Long id) {
        Store store = getById(id);
        return convertToFullResponse(store, 0);
    }

    @Override
    public Page<StoreMiniResponse> getMiniStoreResponse(StoreProductFilter storeFilter) {
        Page<StoreResponse> storeResponses = findAll(storeFilter);
        return storeResponses.map(this::convertToMiniResponse);
    }

    //TODO
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

    private StoreMiniResponse convertToMiniResponse(StoreResponse storeResponse) {
        StoreMiniResponse storeMiniResponse = BeanUtil.copyProperties(storeResponse, StoreMiniResponse.class);
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(storeResponse.getLogo()), ImageSize.LOGO.getWidth(), ImageSize.LOGO.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeMiniResponse.setLogoUrl(imageUrls.get(0));
        }
        storeMiniResponse.setLocationLite(locationService.getLocationResponseLite(storeResponse.getLocationId()));
        return storeMiniResponse;
    }

    private Store getById(Long id) {
        return storeRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store is not found with id [" + id + "]"));
    }

    private List<StoreResponse> getMostActiveStoresByStoreIds(List<Store> stores, int numberOfProducts) {
        return stores.stream()
                .map(store -> convertToFullResponse(store, numberOfProducts))
                .collect(Collectors.toList());
    }

    private StoreResponse convertToResponse(Store store) {
        StoreResponse storeResponse = BeanUtil.copyProperties(store, StoreResponse.class);
        Optional.ofNullable(store.getCompany()).ifPresent(company -> storeResponse.setCompanyId(company.getId()));
        return storeResponse;
    }

    private StoreResponse convertToFullResponse(Store store, int numberOfProducts) {
        StoreResponse storeResponse = convertToResponse(store);
        return convertToFullResponse(store, storeResponse, numberOfProducts);
    }

    private StoreResponse convertToFullResponse(Store store, StoreResponse storeResponse, int numberOfProducts) {
        storeResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127

        setStoreLogo(storeResponse, store.getLogo());
        setStoreLocation(storeResponse, store.getLocationId());
        setStoreProducts(storeResponse, numberOfProducts);
        setStoreCompany(storeResponse, store.getCompany());
        return storeResponse;
    }

    private void setStoreCompany(StoreResponse storeResponse, Company company) {
        Optional.ofNullable(company)
                .ifPresent(com -> storeResponse.setCompany(BeanUtil.copyProperties(com, CompanyResponse.class)));
    }

    private void setStoreLogo(StoreResponse storeResponse, Long logo) {
        if (logo == null) {
            return;
        }
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(logo), ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeResponse.setLogoUrl(imageUrls.get(0));
        }
    }

    private void setStoreLocation(StoreResponse storeResponse, Long locationId) {
        if (locationId != null && storeResponse.getLocation() == null) {
            try {
                storeResponse.setLocation(locationService.findById(locationId));
            } catch (ResourceNotFoundException e) {
                log.debug("Could not found a location with id={}", locationId);
            }
        }
    }

    private void setStoreProducts(StoreResponse storeResponse, int numberOfProducts) {
        if (numberOfProducts > 0) {
            List<ProductResponse> products = getLatestProducts(storeResponse.getId(), numberOfProducts);
            storeResponse.setProducts(products);
        }
    }

    private List<ProductResponse> getLatestProducts(Long storeId, int numberOfProducts) {
        ProductFilter productFilter = new ProductFilter();
        productFilter.setLimit(numberOfProducts);
        productFilter.setStoreId(storeId);
        productFilter.setSorts(new String[] {"-createdAt"});
        return productService.findAll(productFilter).getContent();
    }

    @Override
    public StoreResponseLite getStoreResponseLite(Long id) {
        Store store = getById(id);
        return BeanUtil.copyProperties(store, StoreResponseLite.class);
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public Page<StoreResponse> searchStoresByKeyword(BaseFilter storeFilter){

        return null;
    }

}
