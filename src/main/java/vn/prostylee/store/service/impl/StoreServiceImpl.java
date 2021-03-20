package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.configuration.monitor.annotation.UserBehaviorTracking;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.DateUtils;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.dto.filter.MostActiveStoreFilter;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.filter.StoreProductFilter;
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

import javax.persistence.criteria.Join;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    private final BaseFilterSpecs<Store> baseFilterSpecs;

    private final AuthenticatedProvider authenticatedProvider;

    private final ProductService productService;

    private final FileUploadService fileUploadService;

    private final LocationService locationService;

    private final UserMostActiveService userMostActiveService;

    @Override
    @UserBehaviorTracking
    public Page<StoreResponse> findAll(BaseFilter baseFilter) {
        StoreFilter storeFilter = (StoreFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(storeFilter);
        Page<Store> page = storeRepository.findAllActive(buildSearchable(storeFilter), pageable);
        return page.map(this::convertToResponse);
    }

    @Override
    @UserBehaviorTracking
    public StoreResponse findById(Long id) {
        Store store = getById(id);
        return convertToResponse(store);
    }

    @Override
    public Page<StoreMiniResponse> getMiniStoreResponse(StoreProductFilter storeFilter) {
        Page<StoreResponse> storeResponses = findAll(storeFilter);
        return storeResponses.map(this::convertToMiniResponse);
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

    private StoreMiniResponse convertToMiniResponse(StoreResponse storeResponse) {
        StoreMiniResponse storeMiniResponse = BeanUtil.copyProperties(storeResponse, StoreMiniResponse.class);
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(storeResponse.getLogo()), ImageSize.LOGO.getWidth(), ImageSize.LOGO.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeMiniResponse.setLogoUrl(imageUrls.get(0));
        }
        storeMiniResponse.setLocationLite(locationService.getLocationResponseLite(storeResponse.getLocationId()));
        return storeMiniResponse;
    }

    private StoreResponse convertToResponse(Store store) {
        StoreResponse storeResponse = BeanUtil.copyProperties(store, StoreResponse.class);
        storeResponse.setCompany(BeanUtil.copyProperties(store.getCompany(), CompanyResponse.class));
        return storeResponse;
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

    @Override
    public Page<StoreResponse> getTopProductsOfStores(MostActiveStoreFilter storeFilter) {
        MostActiveRequest request = MostActiveRequest.builder()
                .targetTypes(Collections.singletonList(TargetType.STORE.name()))
                .limit(storeFilter.getLimit())
                .fromDate(DateUtils.getLastDaysBefore(storeFilter.getTimeRangeInDays()))
                .toDate(Calendar.getInstance().getTime())
                .build();

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

    private List<StoreResponse> getMostActiveStoresByStoreIds(List<Store> stores, int numberOfProducts) {
        return stores.stream()
                .map(store -> convert(store, numberOfProducts))
                .collect(Collectors.toList());
    }

    private StoreResponse convert(Store store, int numberOfProducts) {
        StoreResponse storeResponse = convertToResponse(store);
        List<String> imageUrls = fileUploadService.getImageUrls(Collections.singletonList(store.getLogo()), ImageSize.EXTRA_SMALL.getWidth(), ImageSize.EXTRA_SMALL.getHeight());
        if (CollectionUtils.isNotEmpty(imageUrls)) {
            storeResponse.setLogoUrl(imageUrls.get(0));
        }
        storeResponse.setIsAdvertising(false); // TODO Will be implemented after Ads feature completed: https://prostylee.atlassian.net/browse/BE-127
        if (storeResponse.getLocationId() != null) {
            storeResponse.setLocation(locationService.findById(storeResponse.getLocationId()));
        }
        StoreResponse storeProductResponse = BeanUtil.copyProperties(storeResponse, StoreResponse.class);
        List<ProductResponse> products = getLatestProducts(store.getId(), numberOfProducts);
        storeProductResponse.setProducts(products);
        return storeProductResponse;
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
}
