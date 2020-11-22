package vn.prostylee.notification.service;

import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.mail.SimpleMailInfo;

import java.util.concurrent.Future;

public interface EmailService {

	/**
	 * Send email async
	 * @param mailInfo The simple mail metadata for sending
	 */
	boolean send(SimpleMailInfo mailInfo);

	/**
	 * Send email async
	 * @param mailInfo The mail metadata for sending
	 */
	boolean send(MailInfo mailInfo);

	/**
	 * Send email async
	 * @param mailInfo The mail metadata for sending
	 * @param mailTemplate the mail template, supporting both file and string,
	 *                     example: text/mail-template-1.tpl.txt or [( #{greeting(${name})} )]
	 * @param data The values for replacing the markers in the given templates
	 * @param <T> The data type of object data
	 */
	<T> boolean send(MailInfo mailInfo, String mailTemplate, T data);

	/**
	 * Send email async
	 * @param mailInfo The mail metadata for sending
	 * @param mailTemplateConfig the mail templates, including template for subject, content
	 * @param data The values for replacing the markers in the given templates
	 * @param <T> The data type of object data
	 */
	<T> boolean send(MailInfo mailInfo, MailTemplateConfig mailTemplateConfig, T data);

	// Send mail async

	/**
	 * Send email async
	 * @param mailInfo The simple mail metadata for sending
	 */
	Future<Boolean> sendAsync(SimpleMailInfo mailInfo);

	/**
	 * Send email async
	 * @param mailInfo The mail metadata for sending
	 */
	Future<Boolean> sendAsync(MailInfo mailInfo);

	/**
	 * Send email async
	 * @param mailInfo The mail metadata for sending
	 * @param mailTemplate the mail template, supporting both file and string,
	 *                     example: text/mail-template-1.tpl.txt or [( #{greeting(${name})} )]
	 * @param data The values for replacing the markers in the given templates
	 * @param <T> The data type of object data
	 */
	<T> Future<Boolean> sendAsync(MailInfo mailInfo, String mailTemplate, T data);

	/**
	 * Send email async
	 * @param mailInfo The mail metadata for sending
	 * @param mailTemplateConfig the mail templates, including template for subject, content
	 * @param data The values for replacing the markers in the given templates
	 * @param <T> The data type of object data
	 */
	<T> Future<Boolean> sendAsync(MailInfo mailInfo, MailTemplateConfig mailTemplateConfig, T data);
}
