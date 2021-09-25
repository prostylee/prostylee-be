package vn.prostylee.auth.configure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.prostylee.auth.configure.properties.SecurityProperties;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.entity.Feature;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.repository.FeatureRepository;
import vn.prostylee.auth.service.impl.UserDetailsServiceImpl;
import vn.prostylee.core.constant.ApiVersion;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
			// -- swagger ui
			"/v3/api-docs/**",
			"/swagger-config**",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui.html**",
			"/swagger-ui/**",
			"/explorer/**",
			"/webjars/**",
			"/favicon.ico"
	};

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	public AwsCognitoJwtAuthenticationFilter awsCognitoJwtAuthenticationFilter;

	@Autowired
	public JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
				// .anonymous().disable()
				.cors()
				.and()
				.csrf()
				.disable()
				.exceptionHandling()
				.authenticationEntryPoint(unauthorizedHandler)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				// No need authentication.
				.antMatchers(AUTH_WHITELIST).permitAll()
				.antMatchers(securityProperties.getAllowedPublicApis().toArray(new String[0])).permitAll()
				;

		// Need authentication.
		registry = registry.antMatchers(ApiVersion.API_V1 + "/transactions/**").hasAnyRole(AuthRole.SUPER_ADMIN.getRoleName());
		registry = buildDynamicMatchers(registry);
		registry.anyRequest().authenticated() // require authentication for any endpoint that's not whitelisted
		;

		// Add our custom JWT security filter
		http.addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry buildDynamicMatchers(
			ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
		List<Feature> features = featureRepository.findAll();
		for (Feature feature : features) {
			List<String> roles = Optional.ofNullable(feature.getRoles())
					.orElseGet(HashSet::new)
					.stream()
					.map(Role::getCode)
					.collect(Collectors.toList());
			registry = registry.antMatchers(ApiVersion.API_V1 + feature.getApiPath() + "/**").hasAnyRole(roles.toArray(new String[0]));
		}
		return registry;
	}
}
