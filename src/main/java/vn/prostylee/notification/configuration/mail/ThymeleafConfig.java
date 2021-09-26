package vn.prostylee.notification.configuration.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * Configuration for thymeleaf template
 */
@Configuration
public class ThymeleafConfig {

	private static final String MAIL_TEMPLATE_CLASS_PATH = "classpath:/templates/mail/";

	/**
	 * Config mail template with multi-language
	 */
	private final MessageSource messageSource;

	@Autowired
	public ThymeleafConfig(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Config thymeleaf template engine
	 */
	@Bean
	public TemplateEngine emailTemplateEngine() {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		// Resolver for TEXT emails
		templateEngine.addTemplateResolver(textTemplateResolver());
		// Resolver for HTML emails (except the editable one)
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		// Resolver for HTML editable emails (which will be treated as a String)
		templateEngine.addTemplateResolver(stringTemplateResolver());
		// Message source, internationalization specific to emails
		templateEngine.setTemplateEngineMessageSource(messageSource);
		return templateEngine;
	}

	/**
	 * Config send mail template with text file
	 */
	@Bean("textTemplate")
	public ITemplateResolver textTemplateResolver() {
		final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setOrder(1);
		templateResolver.setPrefix(MAIL_TEMPLATE_CLASS_PATH);
		templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
		templateResolver.setSuffix(".tpl.txt");
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateResolver.setCacheable(false);
		templateResolver.setCheckExistence(true);
		return templateResolver;
	}

	/**
	 * Config send mail template with html file
	 */
	@Bean("htmlTemplate")
	public ITemplateResolver htmlTemplateResolver() {
		final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setOrder(2);
		templateResolver.setPrefix(MAIL_TEMPLATE_CLASS_PATH);
		templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
		templateResolver.setSuffix(".tpl.html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		templateResolver.setCacheable(false);
		templateResolver.setCheckExistence(true);
		return templateResolver;
	}

	/**
	 * Config send mail template with string content
	 */
	@Bean("stringTemplate")
	public ITemplateResolver stringTemplateResolver() {
		final StringTemplateResolver templateResolver = new StringTemplateResolver();
		templateResolver.setOrder(3);
		// No resolvable pattern, will simply process as a String template everything not previously matched
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

}