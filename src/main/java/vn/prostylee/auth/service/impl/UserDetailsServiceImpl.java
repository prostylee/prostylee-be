package vn.prostylee.auth.service.impl;

import vn.prostylee.auth.constant.Auth;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.auth.repository.custom.CustomAccountRepository;
import vn.prostylee.core.utils.EncrytedPasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final CustomAccountRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(@Qualifier("customAccountRepository") CustomAccountRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		Account account = userRepository.findByActivatedUsername(email).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
		return this.getUserDetails(account);
	}

	/**
	 * Get the user based on the username
	 * 
	 * @param id The id of user
	 */
	public UserDetails loadUserById(Long id) {
		Account account = userRepository.findOneActive(id).orElseThrow(() -> new ResourceNotFoundException("User with id [" + id + "] is not exists"));
		return this.getUserDetails(account);
	}

	/**
	 * Get user detail if user is valid
	 */
	private UserDetails getUserDetails(Account account) {
		if (StringUtils.isBlank(account.getPassword())) {
			String pwd = EncrytedPasswordUtils.encryptPassword(Auth.GOOGLE_PASSWORD_TOKEN);
			account.setPassword(pwd);
		}
		return new AuthUserDetails(account, this.getFeatures(account));
	}

	private List<Feature> getFeatures(Account account) {
		List<Feature> features = new ArrayList<>();
		account.getRoles().forEach(role -> features.addAll(role.getFeatures()));
		return features;
	}
}
