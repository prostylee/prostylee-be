package vn.prostylee.media.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class ZipFileRequest {

	/**
	 * Attachment type
	 */
	private Integer attachmentType;

	/**
	 * Attachment type name
	 */
	private String attachmentName;

	/**
	 * The URL of file
	 */
	private String attachmentURL;

	/**
	 * The file name
	 */
	private String attachmentTitle;

	/**
	 * The attached date
	 */
	private Date lastAttachmentDate;
}
