package vn.prostylee.media.dto.response;

import com.google.api.services.drive.model.File;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoogleDriveFileResponse implements Serializable {

	/**
	 * Attachment type
	 */
	private Integer type;

	/**
	 * Attachment file name
	 */
	private String fileName;

	/**
	 * Google drive file
	 */
	private File googleDriveFile;

}
