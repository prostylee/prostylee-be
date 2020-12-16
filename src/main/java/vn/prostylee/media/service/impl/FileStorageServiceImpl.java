package vn.prostylee.media.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.media.dto.request.FileStorageRequest;
import vn.prostylee.media.provider.FolderProvider;
import vn.prostylee.media.provider.async.FileStorageAsyncProvider;
import vn.prostylee.media.service.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

	private final FileStorageAsyncProvider fileStorageAsyncProvider;

	private final FolderProvider folderProvider;

	@Autowired
	public FileStorageServiceImpl(FileStorageAsyncProvider fileStorageAsyncProvider, FolderProvider folderHelper) {
		this.fileStorageAsyncProvider = fileStorageAsyncProvider;
		this.folderProvider = folderHelper;
	}

	@Override
	public FileStorageResponse storeFile(String fileName, byte[] bytes) throws IOException {
		return fileStorageAsyncProvider.storeFile(folderProvider.getTemporaryFolder().resolve(fileName), bytes);
	}

	@Override
	public boolean isFileExists(String fileName) {
		return Files.exists(folderProvider.getTemporaryFolder().resolve(fileName));
	}

	@Override
	public List<FileStorageResponse> uploadFiles(FileStorageRequest fileStorageRequest, List<MultipartFile> files, HttpServletRequest request) {
		List<FileStorageResponse> fileStorageResponses = new ArrayList<>();

		if (CollectionUtils.isEmpty(files)) {
			return fileStorageResponses;
		}

		try {
			List<Future<FileStorageResponse>> futures = new ArrayList<>();
			Path fileStorageLocation = folderProvider.getTemporaryFolder();

			// Execute async upload files
			int index = 0;
			for (MultipartFile file : files) {
				if (file.isEmpty()) {
					index++;
					continue;
				}
				int type = this.getAttachmentType(fileStorageRequest, index);
				futures.add(fileStorageAsyncProvider.storeFile(type, fileStorageLocation, file, request));
				index++;
			}

			while (!fileStorageAsyncProvider.isAllFutureDone(futures)) {
				// Wait until done
				// If all are not Done. Pause 100ms for next re-check
				Thread.sleep(AppConstant.WAIT_ASYNC_DONE_IN_MS);
			}

			// Get results
			for (Future<FileStorageResponse> future : futures) {
				fileStorageResponses.add(future.get());
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			throw new ResourceNotFoundException("Can't store files", e);
		}
		return fileStorageResponses;
	}

	@Override
	public boolean deleteFile(String fileName) {
		Path fileStorageLocation = folderProvider.getTemporaryFolder();
		Path filePath = fileStorageLocation.resolve(fileName).normalize();
		try {
			Files.delete(filePath);
			return true;
		} catch (IOException e) {
			log.error("Can't delete file " + fileName);
		}
		return false;
	}

	@Override
	public Resource loadFileAsResource(String fileName) {
		Path fileStorageLocation = folderProvider.getTemporaryFolder();
		return this.loadFileAsResource(fileStorageLocation, fileName);
	}

	@Override
	public Resource loadFileAsResource(Path fileStorageLocation, String fileName) {
		try {
			Path filePath = fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			}
			throw new ResourceNotFoundException("File not found " + fileName);
		} catch (MalformedURLException e) {
			throw new ResourceNotFoundException("File not found " + fileName, e);
		}
	}

	private Integer getAttachmentType(FileStorageRequest fileStorageRequest, int index) {
		if (fileStorageRequest == null || fileStorageRequest.getTypes() == null
				|| index >= fileStorageRequest.getTypes().size()) {
			return null;
		}
		return fileStorageRequest.getTypes().get(index).getType();
	}
}
