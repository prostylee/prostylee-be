package vn.prostylee.media.provider.async;

import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import vn.prostylee.media.dto.request.UploadFileRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.utils.MimeTypeUtil;
import vn.prostylee.media.constant.GoogleMeta;
import vn.prostylee.media.dto.response.GoogleDriveFileResponse;
import vn.prostylee.media.exception.GoogleDriveException;
import vn.prostylee.media.provider.FileProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/**
 * The helper class for communicating files with Google Drive in async mode
 */
@Component
@Slf4j
public class GoogleDriveAsyncProvider extends BaseAsyncProvider {

	private final FileProvider fileProvider;

	private final Drive drive;

	@Autowired
	public GoogleDriveAsyncProvider(FileProvider fileHelper, Drive googleService) {
		this.fileProvider = fileHelper;
		this.drive = googleService;
	}

	/**
	 * Upload files to Google drive
	 *
	 * @param request The request file need to upload
	 * @param folderId The Google folder id will contains the uploaded file
	 * @return The {@link GoogleDriveFileResponse}
	 *
	 * @throws GoogleDriveException if can not upload files to google drive
	 */
	@Async
	public Future<GoogleDriveFileResponse> uploadFile(UploadFileRequest request, String folderId) throws IOException {
		java.io.File fileContent;
		String contentType;
		String fileName = request.getFileName();
		if (request.getFilePath() != null) {
			fileContent = request.getFilePath().toFile();
			String extension = FilenameUtils.getExtension(fileContent.getName());
			contentType = MimeTypeUtil.getMimeType(extension);
		} else {
			MultipartFile file = request.getFile();
			fileContent = fileProvider.convertToFile(file);
			contentType = file.getContentType();
		}
		FileContent mediaContent = new FileContent(contentType, fileContent);

		// Create file for uploading
		File fileNeedToUpload = new File();
		List<String> parents = Collections.singletonList(folderId);
		fileNeedToUpload.setName(fileName).setKind(GoogleMeta.GOOGLE_KIND_OF_FILE).setParents(parents);

		// Upload file to google file
		File googleDriveFile = drive.files().create(fileNeedToUpload, mediaContent)
				//Allow upload to shared drives
				.setSupportsTeamDrives(true).setFields(GoogleMeta.GOOGLE_FIELDS_OF_FILE).execute();
		fileContent.delete();

		// Response
		GoogleDriveFileResponse response = new GoogleDriveFileResponse();
		response.setGoogleDriveFile(googleDriveFile);
		response.setType(request.getType());
		response.setFileName(request.getFileName());
		return new AsyncResult<>(response);
	}

	/**
	 * Delete file from google drive
	 * 
	 * @param fileId The google drive file id
	 * @return True if delete success, otherwise false
	 */
	@Async
	public Future<Boolean> deleteFile(String fileId) throws IOException {
		drive.files().delete(fileId).setSupportsTeamDrives(true).execute();
		return new AsyncResult<>(true);
	}
}
