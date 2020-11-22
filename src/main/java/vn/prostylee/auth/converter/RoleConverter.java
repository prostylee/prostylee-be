package vn.prostylee.auth.converter;

import vn.prostylee.auth.entity.Role;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleConverter {

    public List<String> convert(Collection<Role> roles) {
        return Optional.ofNullable(roles)
                .orElseGet(HashSet::new)
                .stream()
                .map(Role::getCode)
                .collect(Collectors.toList());
    }
}
