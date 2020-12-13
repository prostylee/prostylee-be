package vn.prostylee.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.impl.RoleServiceImpl;

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

        List<RoleResponse> roleResponses = roleService.findAll();
        assertEquals(1, roleResponses.size());
        assertEquals("ADMIN", roleResponses.get(0).getCode());
        assertEquals("Administrator", roleResponses.get(0).getName());
        assertEquals(1L, roleResponses.get(0).getId());
    }

    private Role createRole() {
        Role role = new Role();
        role.setCode("ADMIN");
        role.setId(1L);
        role.setName("Administrator");
        return role;
    }
}
