package vn.prostylee.notification.dto.mail;

import vn.prostylee.core.utils.FileUtil;
import vn.prostylee.core.utils.MimeTypeUtil;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo extends SimpleMailInfo {

	private String from;

	@Setter(AccessLevel.NONE)
	private final List<String> cc = new ArrayList<>();

	@Setter(AccessLevel.NONE)
	private final List<String> bcc = new ArrayList<>();

	private String replyTo;

	private boolean html;

	@Setter(AccessLevel.NONE)
	private final List<MailAttachment> attachments = new ArrayList<>();

	public void addCc(String mail) {
		cc.add(mail);
	}

	public void addBcc(String mail) {
		bcc.add(mail);
	}

	public String[] getCc() {
		return cc.toArray(new String[0]);
	}

	public String[] getBcc() {
		return bcc.toArray(new String[0]);
	}

	// Attachement

	public void addAttachment(String attachmentName, @NonNull String filePath) {
		this.addAttachment(attachmentName, filePath, true, false);
	}

	public void addAttachment(String attachmentName, @NonNull File file) {
		this.addAttachment(attachmentName, file, true, false);
	}

	public void addAttachment(@NonNull String attachmentName, @NonNull byte[] bytes, @NonNull String mimeType) {
		this.addAttachment(attachmentName, bytes, true, false, mimeType);
	}

	// Inline

	public void addInline(String attachmentName, @NonNull String filePath) {
		this.addAttachment(attachmentName, filePath, false, true);
	}

	public void addInline(String attachmentName, @NonNull File file) {
		this.addAttachment(attachmentName, file, false, true);
	}

	public void addInline(@NonNull String attachmentName, @NonNull byte[] bytes, @NonNull String mimeType) {
		this.addAttachment(attachmentName, bytes, false, true, mimeType);
	}

	// Inline Attachement

	public void addAttachmentInline(String attachmentName, @NonNull String filePath) {
		this.addAttachment(attachmentName, filePath, true, true);
	}

	public void addAttachmentInline(String attachmentName, @NonNull File file) {
		this.addAttachment(attachmentName, file, true, true);
	}

	public void addAttachmentInline(@NonNull String attachmentName, @NonNull byte[] bytes, @NonNull String mimeType) {
		this.addAttachment(attachmentName, bytes, true, true, mimeType);
	}

	// Construct attachment

	public void addAttachment(String attachmentName, @NonNull String filePath, boolean isAttachment,
                              boolean isImageInline) {
		MailAttachment att = MailAttachment.builder()
				.name(StringUtils.defaultIfBlank(attachmentName, new File(filePath).getName()))
				.fileIss(FileUtil.getInputStreamSource(filePath))
				.fileAttachment(isAttachment)
				.imageInline(isImageInline)
				.mimeType(MimeTypeUtil.getMimeType(FilenameUtils.getExtension(attachmentName)))
				.build();
		attachments.add(att);
	}

	public void addAttachment(String attachmentName, @NonNull File file,
                              boolean isAttachment, boolean isImageInline) {
		MailAttachment att = MailAttachment.builder()
				.name(StringUtils.defaultIfBlank(attachmentName, file.getName()))
				.fileIss(FileUtil.getInputStreamSource(file))
				.fileAttachment(isAttachment)
				.imageInline(isImageInline)
				.mimeType(MimeTypeUtil.getMimeType(FilenameUtils.getExtension(file.getName())))
				.build();
		attachments.add(att);
	}

	public void addAttachment(String attachmentName, @NonNull byte[] bytes,
                              boolean isAttachment, boolean isImageInline, @NonNull String mimeType) {
		MailAttachment att = MailAttachment.builder()
				.name(StringUtils.defaultIfBlank(attachmentName, "att_" + System.currentTimeMillis()))
				.fileIss(FileUtil.getInputStreamSource(bytes))
				.fileAttachment(isAttachment)
				.imageInline(isImageInline)
				.mimeType(mimeType)
				.build();
		attachments.add(att);
	}

}
