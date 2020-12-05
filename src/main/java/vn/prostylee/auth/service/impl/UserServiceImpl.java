package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseToken;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.repository.UserRepository;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.EncrytedPasswordUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BaseFilterSpecs<User> baseFilterSpecs;

    @Override
    public Page<UserResponse> findAll(BaseFilter baseFilter) {
    	UserFilter userFilter = (UserFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(userFilter);
        Page<User> page = userRepository.getAllUsers(userFilter, pageable);
        return page.map(this::convertToResponse);
    }
    
    @Override
    public UserResponse findById(Long id) {
        User user = getById(id);
        return convertToResponse(user);
    }

    private User getById(Long id) {
        return userRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with id [" + id + "]"));
    }

    @Override
    public UserResponse save(UserRequest userRequest) {
        User user = BeanUtil.copyProperties(userRequest, User.class);
        user.setEmail(user.getUsername());
        user.setPassword(EncrytedPasswordUtils.encryptPassword(userRequest.getPassword()));
        user.setRoles(this.getRoles(userRequest.getRoles()));
        if (user.getAllowNotification() == null) {
            user.setAllowNotification(true);
        }
        User savedUser = userRepository.save(user);
        return BeanUtil.copyProperties(savedUser, UserResponse.class);
    }

    private Set<Role> getRoles(List<String> roleCodes) {
        return Optional.ofNullable(roleCodes)
                .orElseGet(ArrayList::new)
                .stream().map(roleRepository::findByCode)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public UserResponse update(Long id, UserRequest userRequest) {
        User user = getById(id);
        BeanUtil.mergeProperties(userRequest, user);
        user.setEmail(user.getUsername());
        user.setRoles(this.getRoles(userRequest.getRoles()));
        if (StringUtils.isNotBlank(userRequest.getPassword())) {
            user.setPassword(EncrytedPasswordUtils.encryptPassword(userRequest.getPassword()));
        }
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            userRepository.softDelete(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private boolean isFieldValueExists(Object value) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return false;
        }
        return userRepository.findByUsername(value.toString().trim()).isPresent();
    }

    @Override
    public boolean isEntityExists(Long id, Map<String, Object> uniqueValues) {
        final String uniqueFieldName = "username";
        if (uniqueValues == null || uniqueValues.isEmpty() || uniqueValues.get(uniqueFieldName) == null) {
            return false;
        }
        String username = uniqueValues.get(uniqueFieldName).toString();
        if(id == null) {
            return isFieldValueExists(username);
        }
        return userRepository.findByUsername(username)
                .map(User::getId)
                .map(existsId -> existsId.longValue() != id.longValue())
                .orElse(Boolean.FALSE);
    }

    @Override
    public boolean isFieldValueExists(String fieldName, Object value) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return false;
        }
        return userRepository.findByUsername(value.toString().trim()).isPresent();
    }
    
    private Set<String> getRoleCodes(Set<Role> roles) {
    	return roles.stream()
    			.map(Role::getCode)
    			.collect(Collectors.toSet());
    }
    
    private UserResponse convertToResponse(User user) {
    	UserResponse res = BeanUtil.copyProperties(user, UserResponse.class);
        res.setRoles(getRoleCodes(user.getRoles()));
        return res;
    }

    @Override
    public UserResponse findByPushToken(String pushToken) {
        User user = userRepository.findByPushToken(pushToken)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with push token [" + pushToken + "]"));
        return convertToResponse(user);
    }

    @Override
    public User save(FirebaseToken firebaseToken) {
        User user = buildUser(firebaseToken);
        userRepository.save(user);
        return user;
    }

    private User buildUser(FirebaseToken firebaseToken) {
        User user = new User();
        user.setEmail(firebaseToken.getEmail());
        user.setActive(true);
        user.setAllowNotification(true);
        user.setFullName(firebaseToken.getName());
        user.setUsername(firebaseToken.getEmail());
        user.setAvatar(getPicture(firebaseToken));
        Set<UserLinkAccount> sets = buildUserLinkAccounts(firebaseToken, user);
        user.setUserLinkAccounts(sets);
        return user;
    }

    private String getPicture(FirebaseToken firebaseToken) {
        return String.valueOf(firebaseToken.getClaims().get("picture"));
    }

    private Set<UserLinkAccount> buildUserLinkAccounts(FirebaseToken firebaseToken, User user) {
        Set<UserLinkAccount> set = new HashSet<>();
        Map<String, Object> claims = firebaseToken.getClaims();
        Map<String, Object> fireBases = (Map<String, Object>) claims.get("firebase");
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(String.valueOf(claims.get("user_id")))
                .providerName(String.valueOf(fireBases.get("sign_in_provider")))
                .build();
        set.add(userLinkAccount);
        return set;
    }
}
