package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.response.AttributeResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.service.AttributeService;

@Service
@AllArgsConstructor
@Slf4j
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    @Override
    public Attribute getAttributeById(Long id) {
        return attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute is not found with id [" + id + "]"));
    }

    @Override
    public Page<AttributeResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public AttributeResponse findById(Long aLong) {
        return null;
    }

    @Override
    public AttributeResponse save(AttributeRequest attributeRequest) {
        return null;
    }

    @Override
    public AttributeResponse update(Long aLong, AttributeRequest s) {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }
}
