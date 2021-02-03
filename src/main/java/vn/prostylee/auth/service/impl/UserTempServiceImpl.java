package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.dto.response.UserTempResponse;
import vn.prostylee.auth.entity.UserTemp;
import vn.prostylee.auth.repository.UserTempRepository;
import vn.prostylee.auth.service.UserTempService;
import vn.prostylee.core.utils.EncrytedPasswordUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserTempServiceImpl implements UserTempService {

    private final UserTempRepository userTempRepository;
    private final SecurityProperties securityProperties;

    @Override
    public UserTempResponse createUserTemp(String username) {
        String rawPassword = RandomStringUtils.randomNumeric(securityProperties.getResetPasswordPolicies().getLength());
        String encryptedPassword = EncrytedPasswordUtils.encryptPassword(rawPassword);
        Date expiredAt = Date.from(LocalDateTime.now()
                .plusMinutes(securityProperties.getResetPasswordPolicies().getExpiredInMinutes())
                .atZone(ZoneId.systemDefault()).toInstant());

        UserTemp userTemp = new UserTemp();
        userTemp.setUsername(username);
        userTemp.setPassword(encryptedPassword);
        userTemp.setExpiredAt(expiredAt);
        userTempRepository.save(userTemp);

        return UserTempResponse.builder()
                .expiredAt(expiredAt)
                .passwordInPlainText(rawPassword)
                .username(username)
                .expiredInMinutes(securityProperties.getResetPasswordPolicies().getExpiredInMinutes())
                .build();
    }

    @Override
    public boolean isValid(String username, String password) {
        List<UserTemp> userTemps = userTempRepository
                .getUserTemps(username, new Date())
                .orElseGet(ArrayList::new);
        return userTemps.stream().anyMatch(userTemp -> EncrytedPasswordUtils.isMatched(password, userTemp.getPassword()));
    }

    @Override
    public boolean delete(String username) {
        try {
            userTempRepository.deleteByUsername(username);
            return true;
        } catch (EmptyResultDataAccessException e) {
            log.debug("Username {} does not exists", username);
            return false;
        }
    }
}
