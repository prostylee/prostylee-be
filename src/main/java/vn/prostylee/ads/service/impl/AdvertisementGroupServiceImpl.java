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
import vn.prostylee.ads.dto.filter.AdvertisementGroupFilter;
import vn.prostylee.ads.dto.request.AdvertisementGroupRequest;
import vn.prostylee.ads.dto.response.AdvertisementGroupResponse;
import vn.prostylee.ads.entity.AdvertisementGroup;
import vn.prostylee.ads.repository.AdvertisementGroupRepository;
import vn.prostylee.ads.service.AdvertisementGroupService;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdvertisementGroupServiceImpl implements AdvertisementGroupService {

    private final AdvertisementGroupRepository repository;

    private final BaseFilterSpecs<AdvertisementGroup> baseFilterSpecs;

    @Override
    public Page<AdvertisementGroupResponse> findAll(BaseFilter baseFilter) {
        AdvertisementGroupFilter filter = (AdvertisementGroupFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<AdvertisementGroup> page = repository.findAll(buildSearchable(filter), pageable);
        return page.map(this::convertToResponse);
    }

    private Specification<AdvertisementGroup> buildSearchable(AdvertisementGroupFilter filter) {
        Specification<AdvertisementGroup> spec = baseFilterSpecs.search(filter);
        if (BooleanUtils.isTrue(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), true));
        } else if (BooleanUtils.isFalse(filter.getActive())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), false));
        }

        if (StringUtils.isNotBlank(filter.getPosition())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("position"), filter.getPosition()));
        }
        return spec;
    }

    @Cacheable(value = CachingKey.ADS_GROUP, key = "#id")
    @Override
    public AdvertisementGroupResponse findById(Long id) {
        AdvertisementGroup entity = getById(id);
        return convertToResponse(entity);
    }

    private AdvertisementGroupResponse convertToResponse(AdvertisementGroup entity) {
        return BeanUtil.copyProperties(entity, AdvertisementGroupResponse.class);
    }

    private AdvertisementGroup getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AdvertisementGroup is not found with id [" + id + "]"));
    }

    @Override
    public AdvertisementGroupResponse save(AdvertisementGroupRequest request) {
        AdvertisementGroup entity = BeanUtil.copyProperties(request, AdvertisementGroup.class);
        AdvertisementGroup savedEntity = repository.save(entity);
        return convertToResponse(savedEntity);
    }

    @CachePut(cacheNames = CachingKey.ADS_GROUP, key = "#id")
    @Override
    public AdvertisementGroupResponse update(Long id, AdvertisementGroupRequest request) {
        AdvertisementGroup entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        AdvertisementGroup savedEntity = repository.save(entity);
        return convertToResponse(savedEntity);
    }

    @CacheEvict(value = CachingKey.ADS_GROUP, key = "#id")
    @Override
    public boolean deleteById(Long id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a advertisement group without existing in database", e);
            return false;
        }
    }
}
