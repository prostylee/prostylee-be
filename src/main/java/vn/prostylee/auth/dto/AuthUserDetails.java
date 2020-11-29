package vn.prostylee.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.prostylee.auth.dto.response.FeatureResponse;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.utils.BeanUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class AuthUserDetails implements UserDetails {

    private static final long serialVersionUID = -883489449957337175L;

    private Long id;

    private String fullName;

    private String username;

    private String phoneNumber;

    private Character gender;

    @JsonIgnore
    private String password;

    private Set<Role> roles;

    private List<FeatureResponse> features;

    private List<GrantedAuthority> authorities;

    public AuthUserDetails(User user, Collection<Feature> features) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.gender = user.getGender();
        this.roles = user.getRoles();
        this.authorities = createAuthorities(roles);
        this.features = BeanUtil.listCopyProperties(features, FeatureResponse.class);
    }

    private static List<GrantedAuthority> createAuthorities(Set<Role> roles) {
        return Optional.ofNullable(roles)
                .orElseGet(HashSet::new)
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
