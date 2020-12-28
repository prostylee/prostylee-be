package vn.prostylee.media.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.constant.ApiUrl;
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
@RequestMapping(ApiUrl.FILE_UPLOADER)
public class FileUploadController {

	private static final String WIDTH = "w";
	private static final String HEIGHT = "h";
	private final FileUploadService fileUploadService;

	@Autowired
	public FileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

	@GetMapping(value = "/files/{fileIds}")
	public List<String> getFileUrl(
			@RequestParam(required = false, name = WIDTH, defaultValue = "0") int width,
			@RequestParam(required = false, name = HEIGHT, defaultValue = "0") int height,
			@PathVariable(value = "fileIds") List<String> fileIds
	) {
		return fileUploadService.getFiles(fileIds, width, height);
	}

	@PostMapping(value = "/files")
	public List<AttachmentResponse> upload(@NotEmpty @RequestParam("file") List<MultipartFile> files) {
		return fileUploadService.uploadFiles(files);
	}

	@DeleteMapping(value = "/files/{fileId}")
	public boolean delete(@PathVariable(value = "fileId") List<String> fileIds) {
		return fileUploadService.deleteFiles(fileIds);
	}

}
