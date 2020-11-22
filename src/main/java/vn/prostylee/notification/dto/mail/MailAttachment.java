package vn.prostylee.notification.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.InputStreamSource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailAttachment {

	private String name;

	private InputStreamSource fileIss;

	private String mimeType;

	private boolean imageInline;

	private boolean fileAttachment;

}
