package vn.prostylee.notification.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailTemplateConfig {

	/**
	 *
	 * The mail template, supporting both file and string,
	 * example: text/mail-template-1.tpl.txt or [( #{greeting(${name})} )]
	 */
	private String mailSubject;

	/**
	 *
	 * The mail template, supporting both file and string,
	 * example: text/mail-template-1.tpl.txt or [( #{greeting(${name})} )]
	 */
	private String mailContent;

	/**
	 * Determinate whether the mail content is an HTML, by default is false (just a PLAIN TEXT)
	 */
	private Boolean mailIsHtml;

}
