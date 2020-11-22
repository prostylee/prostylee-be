package vn.prostylee.media.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DownloadFileRequest {

	/**
	 * The name of zipped file
	 */
	private String fileName;

	/**
	 * Zip file with group folder by attachment type
	 */
	private Boolean groupByAttachmentType;

	/**
	 * Zip file with including temporary files
	 */
	private Boolean zipFileTemp;

	/**
	 * The list of files will be zipped
	 */
	private List<ZipFileRequest> files;

}
