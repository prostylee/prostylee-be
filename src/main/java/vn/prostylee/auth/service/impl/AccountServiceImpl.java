package vn.prostylee.auth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.filter.AccountFilter;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.auth.dto.request.AccountRequest;
import vn.prostylee.auth.dto.response.AccountResponse;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.repository.custom.CustomAccountRepository;
import vn.prostylee.auth.service.AccountService;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.EncrytedPasswordUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Qualifier("accountService")
public class AccountServiceImpl implements AccountService {

    private CustomAccountRepository accountRepository;

    private RoleRepository roleRepository;

    private BaseFilterSpecs<Account> baseFilterSpecs;

    @Autowired
    public AccountServiceImpl(@Qualifier("customAccountRepository") CustomAccountRepository accountRepository,
                              RoleRepository roleRepository,
                              BaseFilterSpecs<Account> baseFilterSpecs) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.baseFilterSpecs = baseFilterSpecs;
    }

    @Override
    public Page<AccountResponse> findAll(BaseFilter baseFilter) {
    	AccountFilter accountFilter = (AccountFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(accountFilter);
        Page<Account> page = accountRepository.getAllAccounts(accountFilter, pageable);
        return page.map(this::convertToResponse);
    }
    
    @Override
    public AccountResponse findById(Long id) {
        Account account = getById(id);
        return convertToResponse(account);
    }

    private Account getById(Long id) {
        return accountRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with id [" + id + "]"));
    }

    @Override
    public AccountResponse save(AccountRequest userRequest) {
        Account user = BeanUtil.copyProperties(userRequest, Account.class);
        user.setEmail(user.getUsername());
        user.setPassword(EncrytedPasswordUtils.encryptPassword(userRequest.getPassword()));
        user.setRoles(this.getRoles(userRequest.getRoles()));
        if (user.getAllowNotification() == null) {
            user.setAllowNotification(true);
        }
        Account savedUser = accountRepository.save(user);
        return BeanUtil.copyProperties(savedUser, AccountResponse.class);
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
    public AccountResponse update(Long id, AccountRequest userRequest) {
        Account user = getById(id);
        BeanUtil.mergeProperties(userRequest, user);
        user.setEmail(user.getUsername());
        user.setRoles(this.getRoles(userRequest.getRoles()));
        if (StringUtils.isNotBlank(userRequest.getPassword())) {
            user.setPassword(EncrytedPasswordUtils.encryptPassword(userRequest.getPassword()));
        }
        Account savedUser = accountRepository.save(user);
        return convertToResponse(savedUser);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            accountRepository.softDelete(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    private boolean isFieldValueExists(Object value) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return false;
        }
        return accountRepository.findByUsername(value.toString().trim()).isPresent();
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
        return accountRepository.findByUsername(username)
                .map(Account::getId)
                .map(existsId -> existsId.longValue() != id.longValue())
                .orElse(Boolean.FALSE);
    }

    @Override
    public boolean isFieldValueExists(String fieldName, Object value) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return false;
        }
        return accountRepository.findByUsername(value.toString().trim()).isPresent();
    }
    
    private Set<String> getRoleCodes(Set<Role> roles) {
    	return roles.stream()
    			.map(Role::getCode)
    			.collect(Collectors.toSet());
    }
    
    private AccountResponse convertToResponse(Account account) {
    	AccountResponse res = BeanUtil.copyProperties(account, AccountResponse.class);
        res.setRoles(getRoleCodes(account.getRoles()));
        return res;
    }

    @Override
    public AccountResponse findByPushToken(String pushToken) {
        Account account = accountRepository.findByPushToken(pushToken)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found with push token [" + pushToken + "]"));
        return convertToResponse(account);
    }
}
