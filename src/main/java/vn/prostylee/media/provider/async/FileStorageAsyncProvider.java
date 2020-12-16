package vn.prostylee.media.provider.async;

import vn.prostylee.core.utils.UrlUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.media.constant.ApiUrl;
import vn.prostylee.media.provider.FileProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.concurrent.Future;

/**
 * The helper class for storing files on local server at temp folder.
 * The temporary folder is configured in the properties file with key ${app.folder.temp.location}
 */
@Component
public class FileStorageAsyncProvider extends BaseAsyncProvider {

	private final FileProvider fileProvider;

	@Autowired
	public FileStorageAsyncProvider(FileProvider fileHelper) {
		this.fileProvider = fileHelper;
	}

	/**
	 * Perform async storage file to local server
	 */
	@Async
	public Future<FileStorageResponse> storeFile(Integer type, Path fileStorageLocation, MultipartFile file,
                                                 HttpServletRequest request) throws IOException {
		// Normalize file name
		final String originalFilename = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		String storageFileName = fileProvider.createUniqueFileName(extension);

		// Copy file to the target location (Replacing existing file with the same name)
		Path targetLocation = fileStorageLocation.resolve(storageFileName);
		Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

		FileStorageResponse response = new FileStorageResponse();
		response.setType(type);
		response.setDisplayName(originalFilename);
		response.setFilePath(targetLocation.toAbsolutePath().toString());
		response.setFileUrl(this.generateUrlDownloadableFile(storageFileName, request));
		response.setCreatedDate(LocalDateTime.now());
		return new AsyncResult<>(response);
	}

	public FileStorageResponse storeFile(Path targetFile, byte[] bytes) throws IOException {
		Files.copy(new ByteArrayInputStream(bytes), targetFile, StandardCopyOption.REPLACE_EXISTING);

		FileStorageResponse response = new FileStorageResponse();
		response.setDisplayName(targetFile.getFileName().toString());
		response.setFilePath(targetFile.toAbsolutePath().toString());
		response.setCreatedDate(LocalDateTime.now());
		return response;
	}

	/**
	 * Perform async storage file from URL to local server
	 */
	@Async
	public Future<FileStorageResponse> storeFile(Integer type, Path fileStorageLocation, URL url,
			HttpServletRequest request) throws IOException {
		String fileName = FilenameUtils.getName(url.getPath());
		String extension = FilenameUtils.getExtension(url.getPath());
		String storageFileName = fileProvider.createUniqueFileName(extension);

		// Copy file to the target location (Replacing existing file with the same name)
		Path targetLocation = fileStorageLocation.resolve(storageFileName);

		try (InputStream in = url.openStream()) {
			Files.copy(in, targetLocation, StandardCopyOption.REPLACE_EXISTING);
		}

		FileStorageResponse response = new FileStorageResponse();
		response.setType(type);
		response.setDisplayName(fileName);
		response.setFilePath(targetLocation.toAbsolutePath().toString());
		response.setFileUrl(this.generateUrlDownloadableFile(storageFileName, request));
		response.setCreatedDate(LocalDateTime.now());
		return new AsyncResult<>(response);
	}

	/**
	 * Generate an unique url downloadable file
	 * 
	 * @param fileName The file name will be downloaded
	 * @return full url which can be downloaded
	 */
	private String generateUrlDownloadableFile(String fileName, HttpServletRequest request) {
		return UrlUtil.getBaseUrl(request) + ApiUrl.FILE_STORAGE + AppConstant.PATH_SEPARATOR + fileName;
	}
}
