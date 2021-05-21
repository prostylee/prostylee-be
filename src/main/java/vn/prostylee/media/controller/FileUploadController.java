package vn.prostylee.media.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.service.FileUploadService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * The class Cloud Storage controller.
 *
 * At the moment, the application just support Google Drive.
 */
@RestController
@RequestMapping( ApiVersion.API_V1 + "/media")
public class FileUploadController {

	private static final String WIDTH = "w";
	private static final String HEIGHT = "h";
	private final FileUploadService fileUploadService;

	@Autowired
	public FileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	@GetMapping(value = "/images/{ids}")
	public ResponseEntity<List<String>> getFileUrls(
			@RequestParam(required = false, name = WIDTH, defaultValue = "0") int width,
			@RequestParam(required = false, name = HEIGHT, defaultValue = "0") int height,
			@PathVariable(value = "ids") List<Long> fileIds
	) {
		List<String> fileUrls = fileUploadService.getImageUrls(fileIds, width, height);
		if(fileUrls.size() < fileIds.size()) {
			return new ResponseEntity<>(fileUrls, HttpStatus.PARTIAL_CONTENT);
		}
		return new ResponseEntity<>(fileUrls, HttpStatus.OK);
	}

	@PostMapping(value = "/files")
	public ResponseEntity<List<AttachmentResponse>> upload(@NotEmpty @RequestParam("files") List<MultipartFile> files) {
		return ResponseEntity.ok(fileUploadService.uploadFiles(files));
	}

	@DeleteMapping(value = "/files/{fileIds}")
	public ResponseEntity<Boolean> delete(@PathVariable(value = "fileIds") List<Long> fileIds) {
		return ResponseEntity.ok(fileUploadService.deleteFiles(fileIds));
	}

}
