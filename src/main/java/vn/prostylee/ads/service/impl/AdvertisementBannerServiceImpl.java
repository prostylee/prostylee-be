package vn.prostylee.ads.service.impl;

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
import vn.prostylee.ads.dto.filter.AdvertisementBannerFilter;
import vn.prostylee.ads.dto.request.AdvertisementBannerRequest;
import vn.prostylee.ads.dto.request.ImageRequest;
import vn.prostylee.ads.dto.response.AdvertisementBannerResponse;
import vn.prostylee.ads.entity.AdvertisementBanner;
import vn.prostylee.ads.entity.AdvertisementGroup;
import vn.prostylee.ads.repository.AdvertisementBannerRepository;
import vn.prostylee.ads.repository.AdvertisementGroupRepository;
import vn.prostylee.ads.service.AdvertisementBannerService;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.media.service.FileUploadService;

import javax.persistence.criteria.Join;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementBannerServiceImpl implements AdvertisementBannerService {

    private final AdvertisementGroupRepository groupRepository;
    private final AdvertisementBannerRepository campaignRepository;
    private final FileUploadService fileUploadService;
    private final AttachmentService attachmentService;
    private final BaseFilterSpecs<AdvertisementBanner> baseFilterSpecs;

    @Override
    public Page<AdvertisementBannerResponse> findAll(BaseFilter baseFilter) {
        AdvertisementBannerFilter filter = (AdvertisementBannerFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<AdvertisementBanner> page = campaignRepository.findAll(buildSearchable(filter), pageable);
        return page.map(this::convertToResponse);
    }

    private Specification<AdvertisementBanner> buildSearchable(AdvertisementBannerFilter filter) {
        Specification<AdvertisementBanner> spec = baseFilterSpecs.search(filter);

        if (filter.getGroupId() != null) {
            Specification<AdvertisementBanner> joinAdsGroupSpec = (root, query, cb) -> {
                Join<AdvertisementBanner, AdvertisementGroup> group = root.join("group");
                return cb.equal(group.get("id"), filter.getGroupId());
            };
            spec = spec.and(joinAdsGroupSpec);
        }

        return spec;
    }

    @Cacheable(value = CachingKey.ADS_BANNER, key = "#id")
    @Override
    public AdvertisementBannerResponse findById(Long id) {
        AdvertisementBanner entity = getById(id);
        return convertToResponse(entity);
    }

    private AdvertisementBannerResponse convertToResponse(AdvertisementBanner entity) {
        AdvertisementBannerResponse response = BeanUtil.copyProperties(entity, AdvertisementBannerResponse.class);
        response.setBannerImageUrl(buildImageUrls(entity.getBannerImage()));
        Optional.ofNullable(entity.getGroup()).ifPresent(group -> response.setGroupId(group.getId()));
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

    private AdvertisementBanner getById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdvertisementBanner is not found with id [" + id + "]"));
    }

    @Override
    public AdvertisementBannerResponse save(AdvertisementBannerRequest request) {
        AdvertisementBanner entity = BeanUtil.copyProperties(request, AdvertisementBanner.class);

        if (request.getGroupId() != null) {
            AdvertisementGroup group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("AdvertisementGroup is not found with id [" + request.getGroupId() + "]"));
            entity.setGroup(group);
        }

        if (request.getBannerImageInfo() != null) {
            entity.setBannerImage(saveImage(request.getBannerImageInfo()));
        }

        AdvertisementBanner savedEntity = campaignRepository.save(entity);
        return convertToResponse(savedEntity);
    }

    private Long saveImage(ImageRequest imageRequest) {
        return attachmentService.saveAttachmentByNameAndPath(imageRequest.getName(), imageRequest.getPath()).getId();
    }

    @CachePut(cacheNames = CachingKey.ADS_BANNER, key = "#id")
    @Override
    public AdvertisementBannerResponse update(Long id, AdvertisementBannerRequest request) {
        AdvertisementBanner entity = getById(id);
        BeanUtil.mergeProperties(request, entity);

        if (request.getBannerImageInfo() != null) {
            if (entity.getBannerImage() != null) {
                attachmentService.deleteAttachmentsByIdIn(Collections.singletonList(entity.getBannerImage()));
            }
            entity.setBannerImage(saveImage(request.getBannerImageInfo()));
        }

        AdvertisementBanner savedEntity = campaignRepository.save(entity);
        return convertToResponse(savedEntity);
    }

    @CacheEvict(value = CachingKey.ADS_BANNER, key = "#id")
    @Override
    public boolean deleteById(Long id) {
        try {
            campaignRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a advertisement banner without existing in database", e);
            return false;
        }
    }
}
