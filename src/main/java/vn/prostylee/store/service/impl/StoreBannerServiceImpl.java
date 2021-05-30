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
import vn.prostylee.ads.dto.request.ImageRequest;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.media.service.FileUploadService;
import vn.prostylee.store.dto.filter.StoreBannerFilter;
import vn.prostylee.store.dto.request.StoreBannerRequest;
import vn.prostylee.store.dto.response.StoreBannerResponse;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.entity.StoreBanner;
import vn.prostylee.store.repository.StoreBannerRepository;
import vn.prostylee.store.repository.StoreRepository;
import vn.prostylee.store.service.StoreBannerService;

import javax.persistence.criteria.Join;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreBannerServiceImpl implements StoreBannerService {

    private final StoreBannerRepository storeBannerRepository;
    private final FileUploadService fileUploadService;
    private final AttachmentService attachmentService;
    private final BaseFilterSpecs<StoreBanner> baseFilterSpecs;
    private final StoreRepository storeRepository;

    @Override
    public Page<StoreBannerResponse> findAll(BaseFilter baseFilter) {
        StoreBannerFilter filter = (StoreBannerFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<StoreBanner> page = storeBannerRepository.findAll(buildSearchable(filter), pageable);
        return page.map(this::convertToResponse);
    }

    private Specification<StoreBanner> buildSearchable(StoreBannerFilter filter) {
        Specification<StoreBanner> spec = baseFilterSpecs.search(filter);

        if (filter.getStoreId() != null) {
            Specification<StoreBanner> joinAdsGroupSpec = (root, query, cb) -> {
                Join<StoreBanner, Store> group = root.join("store");
                return cb.equal(group.get("id"), filter.getStoreId());
            };
            spec = spec.and(joinAdsGroupSpec);
        }

        return spec;
    }

    @Cacheable(value = CachingKey.ADS_BANNER, key = "#id")
    @Override
    public StoreBannerResponse findById(Long id) {
        StoreBanner entity = getById(id);
        return convertToResponse(entity);
    }

    private StoreBannerResponse convertToResponse(StoreBanner entity) {
        StoreBannerResponse response = BeanUtil.copyProperties(entity, StoreBannerResponse.class);
        response.setBannerImageUrl(buildImageUrls(entity.getBannerImage()));
        Optional.ofNullable(entity.getStore()).ifPresent(group -> response.setStoreId(group.getId()));
        return response;
    }

    private String buildImageUrls(Long bannerImageId) {
        if (bannerImageId != null) {
            try {
                return fileUploadService.getImageUrl(bannerImageId, ImageSize.LARGE.getWidth(), ImageSize.LARGE.getHeight());
            } catch (ResourceNotFoundException e) {
                log.debug("Could not build image Urls from bannerImageId={}", bannerImageId, e);
            }
        }
        return null;
    }

    private StoreBanner getById(Long id) {
        return storeBannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StoreBanner is not found with id [" + id + "]"));
    }

    @Override
    public StoreBannerResponse save(StoreBannerRequest request) {
        StoreBanner entity = BeanUtil.copyProperties(request, StoreBanner.class);

        if (request.getStoreId() != null) {
            Store store = storeRepository.findById(request.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store is not found with id [" + request.getStoreId() + "]"));
            entity.setStore(store);
        }

        if (request.getBannerImageInfo() != null) {
            entity.setBannerImage(saveImage(request.getBannerImageInfo()));
        }

        StoreBanner savedEntity = storeBannerRepository.save(entity);
        return convertToResponse(savedEntity);
    }

    private Long saveImage(ImageRequest imageRequest) {
        return attachmentService.saveAttachmentByNameAndPath(imageRequest.getName(), imageRequest.getPath()).getId();
    }

    @CachePut(cacheNames = CachingKey.ADS_BANNER, key = "#id")
    @Override
    public StoreBannerResponse update(Long id, StoreBannerRequest request) {
        StoreBanner entity = getById(id);
        BeanUtil.mergeProperties(request, entity);

        if (request.getBannerImageInfo() != null) {
            if (entity.getBannerImage() != null) {
                attachmentService.deleteAttachmentsByIdIn(Collections.singletonList(entity.getBannerImage()));
            }
            entity.setBannerImage(saveImage(request.getBannerImageInfo()));
        }

        StoreBanner savedEntity = storeBannerRepository.save(entity);
        return convertToResponse(savedEntity);
    }

    @CacheEvict(value = CachingKey.ADS_BANNER, key = "#id")
    @Override
    public boolean deleteById(Long id) {
        try {
            storeBannerRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a store banner without existing in database", e);
            return false;
        }
    }

    @Override
    public List<StoreBannerResponse> getListStoreBannerByStore(Long storeId){
        List<StoreBanner> storeBannerList = storeBannerRepository.findStoreBannerByStoreId(storeId);
        List<StoreBannerResponse> storeBannerResponses = storeBannerList.stream()
                .map(e -> {
                    return BeanUtil.copyProperties(e,StoreBannerResponse.class);
                }).collect(Collectors.toList());
        return storeBannerResponses;
    }
}

