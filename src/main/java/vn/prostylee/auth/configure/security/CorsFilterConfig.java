package vn.prostylee.auth.configure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import vn.prostylee.auth.configure.properties.SecurityProperties;

@RequiredArgsConstructor
@Configuration
public class CorsFilterConfig {

	private final SecurityProperties securityProperties;

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(securityProperties.isAllowCredentials());
		configuration.setAllowedOrigins(securityProperties.getAllowedOrigins());
		configuration.setAllowedMethods(securityProperties.getAllowedMethods());
		configuration.setAllowedHeaders(securityProperties.getAllowedHeaders());
		configuration.setAllowCredentials(securityProperties.isAllowCredentials());
		configuration.setMaxAge(securityProperties.getMaxAge());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}
}