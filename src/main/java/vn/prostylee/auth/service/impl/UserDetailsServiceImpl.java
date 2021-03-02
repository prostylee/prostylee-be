package vn.prostylee.auth.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.repository.UserRepository;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.EncrytedPasswordUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, ExtUserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(@Qualifier("userRepository") UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByActivatedUsername(email)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
		return this.getUserDetails(user);
	}

	@Override
	public AuthUserDetails loadUserById(Long id) {
		User user = userRepository.findOneActive(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id [" + id + "] is not exists"));
		return this.getUserDetails(user);
	}

	@Override
	public AuthUserDetails loadUserBySub(String sub) {
		User user = userRepository.findActivatedUserBySub(sub)
				.orElseThrow(() -> new ResourceNotFoundException("User with sub [" + sub + "] is not exists"));
		return this.getUserDetails(user);
	}

	/**
	 * Get user detail if user is valid
	 */
	private AuthUserDetails getUserDetails(User user) {
		if (StringUtils.isBlank(user.getPassword())) {
			String pwd = EncrytedPasswordUtils.encryptPassword(AuthConstants.GOOGLE_PASSWORD_TOKEN);
			user.setPassword(pwd);
		}
		return new AuthUserDetails(user, this.getFeatures(user));
	}

	private List<Feature> getFeatures(User user) {
		List<Feature> features = new ArrayList<>();
		user.getRoles().forEach(role -> features.addAll(role.getFeatures()));
		return features;
	}
}
