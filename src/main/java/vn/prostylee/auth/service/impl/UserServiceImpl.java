package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.Gender;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.dto.response.ZaloResponse;
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

    public static final String MALE_RESPONSE_KEY = "male";
    public static final String FEMALE_RESPONSE_KEY = "female";
    public static final String PICTURE_RESPONSE_KEY = "picture";
    public static final String USER_ID_RESPONSE_KEY = "user_id";
    public static final String SIGN_IN_PROVIDER_RESPONSE_KEY = "sign_in_provider";
    public static final String FIREBASE_RESPONSE_KEY = "firebase";

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

    @Override
    public User save(ZaloResponse zaloResponse){
        User user = buildUser(zaloResponse);
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


    private User buildUser(ZaloResponse zaloResponse) {
        User user = new User();
        user.setActive(true);
        user.setAllowNotification(true);
        user.setFullName(zaloResponse.getName());
        user.setUsername(zaloResponse.getId());
        user.setGender(convertGender(zaloResponse));
        user.setAvatar(zaloResponse.getPictureUrl());
        Set<UserLinkAccount> sets = buildUserLinkAccounts(zaloResponse, user);
        user.setUserLinkAccounts(sets);
        return user;
    }

    private Character convertGender(ZaloResponse zaloResponse) {
        if(MALE_RESPONSE_KEY.equalsIgnoreCase(zaloResponse.getGender())){
            return Gender.MALE.getValue();
        }else if(FEMALE_RESPONSE_KEY.equalsIgnoreCase(zaloResponse.getGender())){
            return Gender.FEMALE.getValue();
        }else{
            return Gender.OTHER.getValue();
        }
    }

    private String getPicture(FirebaseToken firebaseToken) {
        return String.valueOf(firebaseToken.getClaims().get(PICTURE_RESPONSE_KEY));
    }

    private Set<UserLinkAccount> buildUserLinkAccounts(FirebaseToken firebaseToken, User user) {
        Set<UserLinkAccount> set = new HashSet<>();
        Map<String, Object> claims = firebaseToken.getClaims();
        Map<String, Object> fireBases = (Map<String, Object>) claims.get(FIREBASE_RESPONSE_KEY);
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(String.valueOf(claims.get(USER_ID_RESPONSE_KEY)))
                .providerName(String.valueOf(fireBases.get(SIGN_IN_PROVIDER_RESPONSE_KEY)))
                .build();
        set.add(userLinkAccount);
        return set;
    }

    private Set<UserLinkAccount> buildUserLinkAccounts(ZaloResponse zaloResponse, User user) {
        Set<UserLinkAccount> set = new HashSet<>();
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(zaloResponse.getId())
                .providerName("zalo")
                .build();
        set.add(userLinkAccount);
        return set;
    }
}
