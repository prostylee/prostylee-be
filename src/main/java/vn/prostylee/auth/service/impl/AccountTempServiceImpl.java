package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.dto.response.AccountTempResponse;
import vn.prostylee.auth.entity.AccountTemp;
import vn.prostylee.auth.repository.AccountTempRepository;
import vn.prostylee.auth.service.AccountTempService;
import vn.prostylee.core.utils.EncrytedPasswordUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountTempServiceImpl implements AccountTempService {

    private final AccountTempRepository accountTempRepository;
    private final SecurityProperties securityProperties;

    @Override
    public AccountTempResponse createAccountTemp(String username) {
        String rawPassword = RandomStringUtils.randomAlphanumeric(securityProperties.getResetPasswordPolicies().getLength());
        String encryptedPassword = EncrytedPasswordUtils.encryptPassword(rawPassword);
        Date expiredAt = Date.from(LocalDateTime.now()
                .plusMinutes(securityProperties.getResetPasswordPolicies().getExpiredInMinutes())
                .atZone(ZoneId.systemDefault()).toInstant());

        AccountTemp accountTemp = new AccountTemp();
        accountTemp.setUsername(username);
        accountTemp.setPassword(encryptedPassword);
        accountTemp.setExpiredAt(expiredAt);
        accountTempRepository.save(accountTemp);

        return AccountTempResponse.builder()
                .expiredAt(expiredAt)
                .passwordInPlainText(rawPassword)
                .username(username)
                .expiredInMinutes(securityProperties.getResetPasswordPolicies().getExpiredInMinutes())
                .build();
    }

    @Override
    public boolean isValid(String username, String password) {
        List<AccountTemp> accountTemps = accountTempRepository
                .getAccountTemps(username, new Date())
                .orElseGet(ArrayList::new);
        return accountTemps.stream().anyMatch(accountTemp -> EncrytedPasswordUtils.isMatched(password, accountTemp.getPassword()));
    }

    @Override
    public boolean delete(String username) {
        accountTempRepository.deleteByUsername(username);
        return true;
    }
}
