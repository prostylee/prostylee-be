package vn.prostylee.auth.service.impl;

import vn.prostylee.auth.constant.AuthConstants;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.repository.UserRepository;
import vn.prostylee.core.exception.ResourceNotFoundException;
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

	private final UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(@Qualifier("userRepository") UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByActivatedUsername(email).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
		return this.getUserDetails(user);
	}

	/**
	 * Get the user based on the username
	 * 
	 * @param id The id of user
	 */
	public UserDetails loadUserById(Long id) {
		User user = userRepository.findOneActive(id).orElseThrow(() -> new ResourceNotFoundException("User with id [" + id + "] is not exists"));
		return this.getUserDetails(user);
	}

	/**
	 * Get user detail if user is valid
	 */
	private UserDetails getUserDetails(User user) {
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
