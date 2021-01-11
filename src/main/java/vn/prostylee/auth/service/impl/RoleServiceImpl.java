package vn.prostylee.auth.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.RoleService;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final BaseFilterSpecs<Role> baseFilterSpecs;

    @Override
    public Page<RoleResponse> findAll(MasterDataFilter filter) {
        Specification<Role> searchable = baseFilterSpecs.search(filter);
        Pageable pageable = baseFilterSpecs.page(filter);
        Page<Role> page = roleRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, RoleResponse.class));

    }
}
