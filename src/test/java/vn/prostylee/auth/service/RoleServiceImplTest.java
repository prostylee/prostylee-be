package vn.prostylee.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.impl.RoleServiceImpl;
import vn.prostylee.core.dto.filter.MasterDataFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void findAll_ReturnRoles() {
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(createRole()));

        Page<RoleResponse> roleResponses = roleService.findAll(new MasterDataFilter());
        assertEquals(1, roleResponses.getSize());
        assertEquals("ADMIN", roleResponses.getContent().get(0).getCode());
        assertEquals("Administrator", roleResponses.getContent().get(0).getName());
        assertEquals(1L, roleResponses.getContent().get(0).getId());
    }

    private Role createRole() {
        Role role = new Role();
        role.setCode("ADMIN");
        role.setId(1L);
        role.setName("Administrator");
        return role;
    }
}
