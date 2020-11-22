package vn.prostylee.media.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Data
public class UploadFileRequest {

	/**
	 * Attachment type
	 */
	private Integer type;

	/**
	 * Attachment file name
	 */
	private String fileName;

	/**
	 * Upload file from local path
	 */
	private Path filePath;

	/**
	 * Upload file from multi-part file
	 */
	private MultipartFile file;

}
