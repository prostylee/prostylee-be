package vn.prostylee.auth.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.RoleService;
import vn.prostylee.core.utils.BeanUtil;

import java.util.List;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponse> findAll() {
        return BeanUtil.listCopyProperties(roleRepository.findAll(), RoleResponse.class);
    }

}
