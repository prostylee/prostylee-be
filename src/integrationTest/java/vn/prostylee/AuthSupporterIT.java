package vn.prostylee;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.constant.Gender;
import vn.prostylee.auth.dto.response.UserCredential;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthSupporterIT {

    public void setAuth() {
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(buildAuth());
    }

    private UsernamePasswordAuthenticationToken buildAuth() {
        UsernamePasswordAuthenticationToken authenticationToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authenticationToken.isAuthenticated()).thenReturn(true);
        when(authenticationToken.getPrincipal()).thenReturn(createUserCredential());
        return authenticationToken;
    }

    private UserCredential createUserCredential() {
        return UserCredential.builder()
                .id(1L)
                .username("test@prostylee.vn")
                .fullName("User test")
                .gender(Gender.MALE.getValue())
                .phoneNumber("0988111222")
                .roles(Collections.singletonList(AuthRole.SUPER_ADMIN.name()))
                .build();
    }
}
