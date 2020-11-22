package vn.prostylee.media.controller;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.utils.MimeTypeUtil;
import vn.prostylee.media.constant.ApiUrl;
import vn.prostylee.media.dto.request.FileStorageRequest;
import vn.prostylee.media.dto.response.FileStorageResponse;
import vn.prostylee.media.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * The class File Storage controller.
 */
@RestController
@RequestMapping(ApiUrl.FILE_STORAGE)
public class FileStorageController {

	private final FileStorageService fileStorageService;

	@Autowired
	public FileStorageController(FileStorageService fileStorageService) {
		this.fileStorageService = fileStorageService;
	}

	@PostMapping
	public ResponseEntity<List<FileStorageResponse>> uploadFileMulti(
            @Valid @RequestPart("requestBody") FileStorageRequest fileStorageRequest,
            @RequestParam("file") MultipartFile[] uploadFiles, HttpServletRequest request) {

		List<MultipartFile> files = Arrays.asList(uploadFiles);
		List<FileStorageResponse> fileStorageResponses = fileStorageService.uploadFiles(fileStorageRequest, files, request);
		return ResponseEntity.ok(fileStorageResponses);
	}

	@GetMapping(ApiUrl.FILE_STORAGE_ACTION)
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Fallback to the default content type if type could not be determined
		String extension = FilenameUtils.getExtension(fileName);
		String contentType = MimeTypeUtil.getMimeType(extension);
		if (StringUtils.isBlank(contentType)) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@DeleteMapping
	public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
		fileStorageService.deleteFile(fileName);
		return ResponseEntity.ok(fileName);
	}
}
