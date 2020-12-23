package vn.prostylee.media.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.constant.ApiUrl;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.service.FileUploadService;

import java.util.Arrays;
import java.util.List;

/**
 * The class Cloud Storage controller.
 *
 * At the moment, the application just support Google Drive.
 */
@RestController
@RequestMapping(ApiUrl.FILE_UPLOADER)
public class FileUploadController {

	private final FileUploadService fileUploadService;

	@Autowired
	public FileUploadController(FileUploadService fileUploadService) {
		this.fileUploadService = fileUploadService;
	}

//	@GetMapping(value = "/files")
//	public List<File> getFiles() {
//		return fileUploadService.getFiles();
//	}
//
//	@GetMapping(value = "/files/{fileId}")
//	public File getFile(@PathVariable(value = "fileId") String fileId) {
//		return fileUploadService.getFile(fileId);
//	}

	@PostMapping(value = "/files")
	public List<AttachmentResponse> upload(@RequestParam("file") MultipartFile[] files) {
		return fileUploadService.uploadFiles(Arrays.asList(files));
	}

	@DeleteMapping(value = "/files/{fileId}")
	public Boolean delete(@PathVariable(value = "fileId") String... fileIds) {
		return fileUploadService.deleteFiles(Arrays.asList(fileIds));
	}

}
