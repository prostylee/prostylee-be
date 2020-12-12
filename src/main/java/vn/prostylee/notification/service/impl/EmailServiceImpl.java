package vn.prostylee.notification.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.prostylee.core.configuration.properties.ServiceProperties;
import vn.prostylee.notification.dto.mail.MailAttachment;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.mail.SimpleMailInfo;
import vn.prostylee.notification.exception.NotificationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import vn.prostylee.notification.service.EmailService;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailServiceImpl implements EmailService {

	private static final String DEFAULT_SUBJECT_CHARSET = StandardCharsets.UTF_8.name();

	private final JavaMailSender emailSender;

	private final SpringTemplateEngine templateEngine;

	private final ServiceProperties serviceProperties;

	@Override
	public boolean send(SimpleMailInfo mailInfo) {
		try {
			return this.sendAsync(mailInfo).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new NotificationException("Can't send a simple email with the given info " + mailInfo, e);
		}
	}

	@Override
	public boolean send(MailInfo mailInfo) {
		try {
			return this.sendAsync(mailInfo).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new NotificationException("Can't send an email with the given info " + mailInfo, e);
		}
	}

	@Override
	public <T> boolean send(MailInfo mailInfo, String mailTemplate, T data) {
		try {
			return this.sendAsync(mailInfo, mailTemplate, data).get();
		} catch (InterruptedException | ExecutionException e) {
			String message = MessageFormat.format(
					"Cannot send an email with mailInfo {0}, mailTemplate {1}, data {2}",
					mailInfo, mailTemplate, data);
			throw new NotificationException(message, e);
		}
	}

	@Override
	public <T> boolean send(MailInfo mailInfo, MailTemplateConfig mailTemplateConfig, T data) {
		try {
			return this.sendAsync(mailInfo, mailTemplateConfig, data).get();
		} catch (InterruptedException | ExecutionException e) {
			String message = MessageFormat.format(
					"Cannot send an email with mailInfo {0}, mailTemplateConfig {1}, data {2}",
					mailInfo, mailTemplateConfig, data);
			throw new NotificationException(message, e);
		}
	}

	@Async
	@Override
	public Future<Boolean> sendAsync(SimpleMailInfo mailInfo) {
		log.debug("Send a simple email " + mailInfo);
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					DEFAULT_SUBJECT_CHARSET);

			helper.setFrom(serviceProperties.getEmail().getSendFrom());
			helper.setTo(mailInfo.getTo());
			helper.setSubject(mailInfo.getSubject());
			helper.setText(mailInfo.getContent(), false);

			emailSender.send(message);
			return new AsyncResult<>(true);
		} catch (MailException | MessagingException e) {
			throw new NotificationException("Can't send an email async with the given info " + mailInfo, e);
		}
	}

	@Async
	@Override
	public Future<Boolean> sendAsync(@NonNull MailInfo mailInfo) {
		log.debug("Send an email mailInfo " + mailInfo);
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,
					MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					DEFAULT_SUBJECT_CHARSET);

			helper.setFrom(StringUtils.defaultIfBlank(mailInfo.getFrom(), serviceProperties.getEmail().getSendFrom()));
			helper.setReplyTo(StringUtils.defaultIfBlank(mailInfo.getReplyTo(), serviceProperties.getEmail().getReplyTo()));
			helper.setTo(mailInfo.getTo());
			helper.setSubject(mailInfo.getSubject());
			helper.setText(mailInfo.getContent(), mailInfo.isHtml());

			if (mailInfo.getCc().length > 0) {
				helper.setCc(mailInfo.getCc());
			}

			if (mailInfo.getBcc().length > 0) {
				helper.setBcc(mailInfo.getBcc());
			}

			if (!CollectionUtils.isEmpty(mailInfo.getAttachments())) {
				for (MailAttachment att : mailInfo.getAttachments()) {
					MimeBodyPart bodyPart = this.getMimeBodyPart(att);
					if (att.isFileAttachment()) {
						helper.getMimeMultipart().addBodyPart(bodyPart);
					}
					if (att.isImageInline()) {
						helper.addInline(att.getName(), att.getFileIss(), att.getMimeType());
					}
				}
			}

			emailSender.send(message);
			return new AsyncResult<>(true);
		} catch (MailException | MessagingException e) {
			throw new NotificationException("Can't send an email async with the given info " + mailInfo, e);
		}
	}

	@Async
	@Override
	public <T> Future<Boolean> sendAsync(@NonNull MailInfo mailInfo, @NonNull String mailTemplate, T data) {
		log.debug(MessageFormat.format("Send an email mailInfo {0}, mailTemplate {1}, data {2}", mailInfo, mailTemplate, data));
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);

		Map<String, Object> mailVariables = this.convertObjectToMap(data);
		String mailContent = this.replaceThymeleafMarker(mailTemplate, context, mailVariables);
		mailInfo.setContent(mailContent);

		return this.sendAsync(mailInfo);
	}

	@Async
	@Override
	public <T> Future<Boolean> sendAsync(@NonNull MailInfo mailInfo, @NonNull MailTemplateConfig mailTemplateConfig, T data) {
		log.debug(MessageFormat.format("Send an email mailInfo {0}, mailTemplateConfig {1}, data {2}", mailInfo, mailTemplateConfig, data));
		Locale locale = LocaleContextHolder.getLocale();
		Context context = new Context(locale);

		Map<String, Object> mailVariables = this.convertObjectToMap(data);
		if (StringUtils.isNotBlank(mailTemplateConfig.getMailSubject())) {
			String mailSubject = this.replaceThymeleafMarker(mailTemplateConfig.getMailSubject(), context, mailVariables);
			mailInfo.setSubject(mailSubject);
		}

		if (StringUtils.isNotBlank(mailTemplateConfig.getMailContent())) {
			String mailContent = this.replaceThymeleafMarker(mailTemplateConfig.getMailContent(), context, mailVariables);
			mailInfo.setContent(mailContent);
			mailInfo.setHtml(mailTemplateConfig.getMailIsHtml());
		}

		return this.sendAsync(mailInfo);
	}

	private MimeBodyPart getMimeBodyPart(MailAttachment att) {
		try {
			MimeBodyPart attachment = new MimeBodyPart();
			DataSource dataSource = new ByteArrayDataSource(att.getFileIss().getInputStream(), att.getMimeType());
			DataHandler dataHandler = new DataHandler(dataSource);
			attachment.setDataHandler(dataHandler);
			attachment.setFileName(att.getName());
			attachment.setDisposition(Part.ATTACHMENT);
			return attachment;
		} catch (IOException | MessagingException e) {
			throw new NotificationException("Can't send an email with attachment " + att.getName(), e);
		}
	}

	/**
	 * Convert java object to map
	 * 
	 * @param data The object data
	 * @return the map with key value of the given object data
	 */
	private <T> Map<String, Object> convertObjectToMap(T data) {
		if (data == null) {
			return new HashMap<>();
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(data, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * Replace mail marker by thymeleaf
	 * 
	 * @param thymeleafMarker The thymeleaf template with markers
	 * @param context The thymeleaf process context
	 * @param vars The parameters for replacing the markers
	 * @return The string after replacing values for the markers
	 */
	private String replaceThymeleafMarker(String thymeleafMarker, Context context, Map<String, Object> vars) {
		context.setVariables(vars);
		return templateEngine.process(thymeleafMarker, context);
	}

}