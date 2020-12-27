package vn.prostylee.media.controller;

import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.constant.ApiUrl;
import vn.prostylee.media.dto.request.DownloadFileRequest;
import vn.prostylee.media.service.CloudStorageService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * The class Cloud Storage controller.
 *
 * At the moment, the application just support Google Drive.
 */
@RestController
@RequestMapping(ApiUrl.CLOUD_STORAGE)
public class CloudStorageController {

	private final CloudStorageService googleApi;

	@Autowired
	public CloudStorageController(CloudStorageService googleApi) {
		this.googleApi = googleApi;
	}

	@GetMapping(value = "/files")
	public List<File> getFiles() {
		return googleApi.getFiles();
	}

	@GetMapping(value = "/files/{fileId}")
	public File getFile(@PathVariable(value = "fileId") String fileId) {
		return googleApi.getFile(fileId);
	}

	@GetMapping(value = "/folders/{folderId}")
	public List<File> getFiles(@PathVariable(value = "folderId") String folderId) {
		return googleApi.getFilesInFolder(folderId);
	}

	@GetMapping(value = "/files/{fileId}/download")
	public byte[] download(@PathVariable(value = "fileId") String fileId) {
		return googleApi.downloadFile(fileId);
	}

	@PostMapping(value = "/folders/download")
	public ResponseEntity<Void> downloadAll(@Valid @RequestBody DownloadFileRequest request,
                                            HttpServletResponse response) throws IOException {
		googleApi.zipAndDownloadAll(request, response);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/files")
	public List<File> upload(@RequestParam("file") MultipartFile[] files) {
		return googleApi.uploadFiles(files);
	}

	@DeleteMapping(value = "/files/{fileId}")
	public Boolean delete(@PathVariable(value = "fileId") String fileId) {
		return googleApi.deleteFile(fileId);
	}

	@PostMapping(value = "folders")
	public File createFolder(@RequestParam(value = "folderName") String folderName,
                             @RequestParam(value = "parentId", required = false) String parentId) {
		return googleApi.createFolder(folderName, parentId);
	}
}
