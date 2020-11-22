package vn.prostylee.media.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.request.FileStorageRequest;
import vn.prostylee.media.dto.response.FileStorageResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileStorageService {

	/**
	 * Save all files to local server
	 */
	List<FileStorageResponse> uploadFiles(FileStorageRequest fileStorageRequest, List<MultipartFile> files, HttpServletRequest request);

	FileStorageResponse storeFile(String fileName, byte[] bytes) throws IOException;

	boolean isFileExists(String fileName);

	/**
	 * Delete file from  local server
	 */
	boolean deleteFile(String fileName);
	
	/**
	 * Load file as source
	 */
	Resource loadFileAsResource(String fileName);

	/**
	 * Load file as source
	 */
	Resource loadFileAsResource(Path fileStorageLocation, String fileName);
}
