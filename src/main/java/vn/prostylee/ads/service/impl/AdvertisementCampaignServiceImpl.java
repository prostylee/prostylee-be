package vn.prostylee.ads.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.ads.dto.filter.AdvertisementCampaignFilter;
import vn.prostylee.ads.dto.request.AdvertisementCampaignRequest;
import vn.prostylee.ads.dto.request.ImageRequest;
import vn.prostylee.ads.dto.response.AdvertisementCampaignResponse;
import vn.prostylee.ads.entity.AdvertisementCampaign;
import vn.prostylee.ads.entity.AdvertisementGroup;
import vn.prostylee.ads.repository.AdvertisementCampaignRepository;
import vn.prostylee.ads.repository.AdvertisementGroupRepository;
import vn.prostylee.ads.service.AdvertisementCampaignService;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.location.dto.request.LocationRequest;
import vn.prostylee.location.service.LocationService;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.media.service.FileUploadService;

import javax.persistence.criteria.Join;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementCampaignServiceImpl implements AdvertisementCampaignService {

    private final AdvertisementGroupRepository groupRepository;
    private final AdvertisementCampaignRepository campaignRepository;
    private final LocationService locationService;
    private final FileUploadService fileUploadService;
    private final AttachmentService attachmentService;
    private final BaseFilterSpecs<AdvertisementCampaign> baseFilterSpecs;

    @Override
    public Page<AdvertisementCampaignResponse> findAll(BaseFilter baseFilter) {
        AdvertisementCampaignFilter filter = (AdvertisementCampaignFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<AdvertisementCampaign> page = campaignRepository.findAll(buildSearchable(filter), pageable);
        return page.map(this::convertToResponse);
    }

    private Specification<AdvertisementCampaign> buildSearchable(AdvertisementCampaignFilter filter) {
        Specification<AdvertisementCampaign> spec = baseFilterSpecs.search(filter);

        if (BooleanUtils.isTrue(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));
        } else if (BooleanUtils.isFalse(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), false));
        }

        if (StringUtils.isNotBlank(filter.getPosition())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("position"), filter.getPosition()));
        }

        if (StringUtils.isNotBlank(filter.getTargetType())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("targetType"), filter.getTargetType()));
        }

        if (filter.getGroupId() != null) {
            Specification<AdvertisementCampaign> joinAdsGroupSpec = (root, query, cb) -> {
                Join<AdvertisementCampaign, AdvertisementGroup> group = root.join("group");
                return cb.equal(group.get("id"), filter.getGroupId());
            };
            spec = spec.and(joinAdsGroupSpec);
        }

        return spec;
    }

    @Cacheable(value = CachingKey.ADS_CAMPAIGN, key = "#id")
    @Override
    public AdvertisementCampaignResponse findById(Long id) {
        AdvertisementCampaign entity = getById(id);
        return convertToResponse(entity);
    }

    private AdvertisementCampaignResponse convertToResponse(AdvertisementCampaign entity) {
        AdvertisementCampaignResponse response = BeanUtil.copyProperties(entity, AdvertisementCampaignResponse.class);
        response.setFeatureImageUrl(buildImageUrls(response.getFeatureImage()));
        Optional.ofNullable(entity.getGroup()).ifPresent(group -> response.setGroupId(group.getId()));
        return response;
    }

    private AdvertisementCampaign getById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdvertisementCampaign is not found with id [" + id + "]"));
    }

    private String buildImageUrls(Long featureImageId) {
        if (featureImageId != null) {
            try {
                return fileUploadService.getImageUrl(featureImageId, ImageSize.LARGE.getWidth(), ImageSize.LARGE.getHeight());
            } catch (ResourceNotFoundException e) {
                log.debug("Could not build image Urls from featureImageId={}", featureImageId, e);
            }
        }
        return null;
    }

    @Override
    public AdvertisementCampaignResponse save(AdvertisementCampaignRequest request) {
        AdvertisementCampaign entity = BeanUtil.copyProperties(request, AdvertisementCampaign.class);

        if (request.getGroupId() != null) {
            AdvertisementGroup group = groupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("AdvertisementGroup is not found with id [" + request.getGroupId() + "]"));
            entity.setGroup(group);
        }

        if (request.getTargetLocation() != null) {
            Long locationId = saveLocation(request.getTargetLocation());
            entity.setTargetLocationId(locationId);
        }

        if (request.getFeatureImageInfo() != null) {
            entity.setFeatureImage(saveImage(request.getFeatureImageInfo()));
        }

        AdvertisementCampaign savedEntity = campaignRepository.save(entity);
        return convertToResponse(savedEntity);
    }

    private Long saveLocation(LocationRequest locationRequest) {
        return locationService.save(locationRequest).getId();
    }

    private Long saveImage(ImageRequest imageRequest) {
        return attachmentService.saveAttachmentByNameAndPath(imageRequest.getName(), imageRequest.getPath()).getId();
    }

    @CachePut(cacheNames = CachingKey.ADS_CAMPAIGN, key = "#id")
    @Override
    public AdvertisementCampaignResponse update(Long id, AdvertisementCampaignRequest request) {
        AdvertisementCampaign entity = getById(id);
        BeanUtil.mergeProperties(request, entity);

        if (request.getTargetLocation() != null) {
            if (entity.getTargetLocationId() != null) {
                locationService.deleteById(entity.getTargetLocationId());
            }
            Long locationId = saveLocation(request.getTargetLocation());
            entity.setTargetLocationId(locationId);
        }

        if (request.getFeatureImageInfo() != null) {
            if (entity.getFeatureImage() != null) {
                attachmentService.deleteAttachmentsByIdIn(Collections.singletonList(entity.getFeatureImage()));
            }
            entity.setFeatureImage(saveImage(request.getFeatureImageInfo()));
        }

        AdvertisementCampaign savedEntity = campaignRepository.save(entity);
        return convertToResponse(savedEntity);
    }

    @CacheEvict(value = CachingKey.ADS_CAMPAIGN, key = "#id")
    @Override
    public boolean deleteById(Long id) {
        try {
            campaignRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a advertisement campaign without existing in database", e);
            return false;
        }
    }
}
