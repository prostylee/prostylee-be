package vn.prostylee.core.configuration.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import vn.prostylee.core.configuration.properties.SwaggerProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * This class used to configure the API documents with Swagger 2
 *
 * Check at http://localhost:8090/swagger-ui.html
 */
@RequiredArgsConstructor
@Configuration
@Profile(value = { "dev", "test", "staging" })
public class Swagger3Configuration {

	private static final String TOKEN_HEADER = "JWT-Token";
	private final SwaggerProperties swaggerProperties;

	@Bean
	public OpenAPI secureApi() {
		Contact contact = new Contact();
		contact.setName(swaggerProperties.getContactName());
		contact.setUrl(swaggerProperties.getContactUrl());
		contact.setEmail(swaggerProperties.getContactEmail());

		List<SecurityRequirement> security = new ArrayList<>();
		security.add(new SecurityRequirement().addList(TOKEN_HEADER));

		return new OpenAPI()
				.info(new Info().title(swaggerProperties.getTitle())
						.description(swaggerProperties.getDescription())
						.termsOfService(swaggerProperties.getTermsOfServiceUrl())
						.contact(contact)
						.version(swaggerProperties.getVersion())
						.license(new License().name(swaggerProperties.getLicense()).url(swaggerProperties.getLicenseUrl()))
				).components(new Components()
					.addSecuritySchemes(TOKEN_HEADER, new SecurityScheme()
							.type(SecurityScheme.Type.HTTP)
							.scheme("bearer")
							.bearerFormat("JWT")
							.name("Authorization")
					)
				).security(security);
	}
}