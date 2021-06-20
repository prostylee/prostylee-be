package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.ads.dto.request.ImageRequest;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.BasicUserResponse;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.repository.UserRepository;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.EncrytedPasswordUtils;
import vn.prostylee.media.constant.ImageSize;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.media.service.FileUploadService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BaseFilterSpecs<User> baseFilterSpecs;
    private final AttachmentService attachmentService;
    private final FileUploadService fileUploadService;

    @Override
    public Page<UserResponse> findAll(BaseFilter baseFilter) {
    	UserFilter userFilter = (UserFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(userFilter);
        Page<User> page = userRepository.findAll(buildSearchable(userFilter), pageable);
        return page.map(this::convertToResponse);
    }

    private Specification<User> buildSearchable(UserFilter userFilter) {
        Specification<User> spec = baseFilterSpecs.search(userFilter);

        if (CollectionUtils.isNotEmpty(userFilter.getRoleCodes())) {
            Specification<User> additionalSpec = (root, query, cb) -> {
                // Build Many to Many query
                query.distinct(true);
                Root<Role> roleRoot = query.from(Role.class);
                Expression<Collection<User>> userRoles = roleRoot.get("users");
                CriteriaBuilder.In<String> inClause = cb.in(roleRoot.get("code"));
                for (String roleCode : userFilter.getRoleCodes()) {
                    inClause.value(roleCode);
                }
                return cb.and(inClause, cb.isMember(root, userRoles));
            };
            spec = spec.and(additionalSpec);
        }

        return spec;
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
        if (StringUtils.isNotBlank(userRequest.getPassword())) {
            user.setPassword(EncrytedPasswordUtils.encryptPassword(userRequest.getPassword()));
        }
        if (user.getAllowNotification() == null) {
            user.setAllowNotification(true);
        }
        if (StringUtils.isBlank(userRequest.getSub())) {
            user.setSub(UUID.randomUUID().toString());
        }
        user.setRoles(this.getRoles(userRequest.getRoles()));
        User savedUser = this.save(user);
        UserResponse response = BeanUtil.copyProperties(savedUser, UserResponse.class);
        response.setRoles(user.getRoles().stream().map(Role::getCode).collect(Collectors.toSet()));
        return response;
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
        user.setRoles(this.getRoles(userRequest.getRoles()));
        if (StringUtils.isNotBlank(userRequest.getPassword())) {
            user.setPassword(EncrytedPasswordUtils.encryptPassword(userRequest.getPassword()));
        }
        if (userRequest.getAvatarImageInfo() != null) {
            Long avatarId = saveImage(userRequest.getAvatarImageInfo());
            String avatarUrl = fileUploadService.getImageUrl(avatarId, ImageSize.FULL.getWidth(), ImageSize.FULL.getHeight());
            user.setAvatar(avatarUrl);
        }
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }

    private Long saveImage(ImageRequest imageRequest) {
        return attachmentService.saveAttachmentByNameAndPath(imageRequest.getName(), imageRequest.getPath()).getId();
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
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isExistsUserByEmail(String email) {
        return userRepository.findActivatedUserByEmail(email).isPresent();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findActivatedUserByEmail(email);
    }

    @Override
    public Optional<User> findBySub(String sub) {
        return userRepository.findActivatedUserBySub(sub);
    }

    @Override
    public List<UserResponse> findUsersByIds(List<Long> userIds) {
        return userRepository.findUsersByIds(userIds)
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BasicUserResponse> getBasicUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(user -> BeanUtil.copyProperties(user, BasicUserResponse.class));
    }

}
