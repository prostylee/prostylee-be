package vn.prostylee.media.service.impl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.configuration.properties.GoogleDriveProperties;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.utils.UrlUtil;
import vn.prostylee.media.constant.ApiUrl;
import vn.prostylee.media.constant.GoogleMeta;
import vn.prostylee.media.dto.request.DownloadFileRequest;
import vn.prostylee.media.dto.request.UploadFileRequest;
import vn.prostylee.media.dto.request.ZipFileRequest;
import vn.prostylee.media.dto.response.GoogleDriveFileResponse;
import vn.prostylee.media.exception.GoogleDriveException;
import vn.prostylee.media.provider.FolderProvider;
import vn.prostylee.media.provider.async.GoogleDriveAsyncProvider;
import vn.prostylee.media.service.CloudStorageService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This provider class for communicating with Google Drive
 *
 * @see <a href="https://developers.google.com/drive/api/v3/about-sdk">https://developers.google.com/drive/api/v3/about-sdk</a>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleDriveServiceImpl implements CloudStorageService {

	private final Drive drive;

	private final GoogleDriveAsyncProvider googleDriveAsyncProvider;

	private final GoogleDriveProperties googleDriveProperties;

	private final FolderProvider folderProvider;


	@Override
	public List<File> getFiles() {
		return this.getFilesInFolder(googleDriveProperties.getFolderId());
	}

	@Override
	public List<File> getFilesInFolder(String folderId) {
		List<File> files;
		try {
			FileList result = drive.files().list()
					.setFields("files(" + GoogleMeta.GOOGLE_FIELDS_OF_FILE + ")")
					.setQ("'" + folderId + "' in parents and trashed=false").execute();

			files = result.getFiles();
		} catch (Exception e) {
			throw new GoogleDriveException("Can't get files in folder " + folderId, e);
		}
		if (CollectionUtils.isEmpty(files)) {
			log.debug("GetFiles is empty files");
			return new ArrayList<>();
		}
		return files;
	}

	@Override
	public List<File> uploadFiles(MultipartFile... files) {
		return this.uploadFiles(googleDriveProperties.getFolderId(), files);
	}

	/**
	 * Upload files to the given google folder id.
	 * This method will execute uploading in async mode.
	 *
	 * @param files The {@link MultipartFile}
	 * @param folderId The google folder id will stored the given files
	 * @return The list of {@link File} after uploaded
	 */
	@Override
	public List<File> uploadFiles(String folderId, MultipartFile... files) {
		if (files == null || files.length == 0) {
			return new ArrayList<>();
		}
		try {
			UploadFileRequest[] requests = buildUploadFileRequests(files);
			List<GoogleDriveFileResponse> responses = this.uploadFilesAsync(folderId, requests);
			return responses.stream().map(GoogleDriveFileResponse::getGoogleDriveFile).collect(Collectors.toList());
		} catch (GoogleDriveException e) {
			throw new GoogleDriveException("Can't upload files to google drive", e);
		}
	}

	private UploadFileRequest[] buildUploadFileRequests(MultipartFile... files) {
		if(files == null || files.length < 1) {
			return new UploadFileRequest[0];
		}

		UploadFileRequest[] requests = new UploadFileRequest[files.length];
		for(int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			UploadFileRequest gdRequest = new UploadFileRequest();
			gdRequest.setFile(file);
			gdRequest.setFileName(file.getOriginalFilename());
			requests[i] = gdRequest;
		}
		return requests;
	}

	/**
	 * Download file of the given google file id
	 *
	 * @param fileId The google file id
	 * @return The byte array of downloaded file
	 */
	@Override
	public byte[] downloadFile(String fileId) {
		byte[] bytes = null;
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
			bytes = outputStream.toByteArray();
		} catch (Exception ex) {
			log.error("DownloadFile Google API exception: ", ex);
		}
		return bytes;
	}

	/**
	 * Perform delete a Google file
	 *
	 * @param fileId The Google file id will be deleted
	 * @return true if deleted successfully, otherwise false
	 */
	@Override
	public boolean deleteFile(String fileId) {
		try {
			return googleDriveAsyncProvider.deleteFile(fileId).get();
		} catch (InterruptedException | ExecutionException | IOException e) {
			throw new GoogleDriveException("Can not delete file " + fileId, e);
		}
	}

	/**
	 * Get a File with the given Google file id
	 *
	 * @param fileId The Google file id
	 * @return The {@link File}
	 */
	@Override
	public File getFile(String fileId) {
		try {
			return drive.files().get(fileId)
					.setFields(GoogleMeta.GOOGLE_FIELDS_OF_FILE).execute();
		} catch (Exception e) {
			throw new GoogleDriveException("Can't get file " + fileId, e);
		}
	}

	/**
	 * Create folder on Google drive
	 *
	 * @param folderName The folder name will be created
	 * @return The folder {@link File} after created or null if error while creating folder
	 */
	@Override
	public File createFolder(String folderName) {
		return createFolder(folderName, null);
	}

	/**
	 * Create folder on Google drive
	 *
	 * @param folderName The folder name will be created
	 * @param parentId The parent Google folder id. If the given id is null, it will be created in default setting folder
	 * @return The folder {@link File} after created or null if error while creating folder
	 */
	@Override
	public File createFolder(String folderName, @Nullable String parentId) {

		String parentFolderId = Optional.ofNullable(parentId).orElseGet(googleDriveProperties::getFolderId);
		try {
			File fileMetadata = new File();
			fileMetadata.setName(folderName);
			fileMetadata.setMimeType(GoogleMeta.GOOGLE_KIND_OF_FOLDER);
			fileMetadata.setParents(Collections.singletonList(parentFolderId));
			return drive.files().create(fileMetadata)
					.setFields(GoogleMeta.GOOGLE_FIELDS_OF_FILE).execute();
		} catch (IOException e) {
			throw new GoogleDriveException("Can't create folder " + folderName + " inside " + parentFolderId, e);
		}
	}

	/**
	 * Perform async storage file to Google drive
	 *
	 * @param folderId The Google folder id will contains the uploaded files
	 * @param requests The request files need to upload
	 * @return The {@link GoogleDriveFileResponse}
	 *
	 * @throws GoogleDriveException if can not upload files to google drive
	 */
	@Override
	public List<GoogleDriveFileResponse> uploadFilesAsync(String folderId, UploadFileRequest... requests) {
		List<GoogleDriveFileResponse> responses = new ArrayList<>();

		if (requests == null || requests.length < 1) {
			return responses;
		}

		try {
			List<Future<GoogleDriveFileResponse>> futures = new ArrayList<>();
			// Execute async upload files
			for (UploadFileRequest request : requests) {
				futures.add(googleDriveAsyncProvider.uploadFile(request, folderId));
			}

			while (!googleDriveAsyncProvider.isAllFutureDone(futures)) {
				// Wait until done
				// If all are not Done. Pause 100ms for next re-check
				Thread.sleep(AppConstant.WAIT_ASYNC_DONE_IN_MS);
			}

			// Get results
			for (Future<GoogleDriveFileResponse> future : futures) {
				responses.add(future.get());
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			this.rollbackAsync(responses);
			throw new GoogleDriveException("Can't store files to google drive ", e);
		}
		return responses;
	}

	/**
	 * Perform delete files from Google drive
	 *
	 * @param  fileIds The list of Google file ids will be deleted
	 *
	 * @throws GoogleDriveException if can not delete files from google drive
	 */
	@Override
	public boolean deleteFilesAsync(List<String> fileIds) {
		if (CollectionUtils.isEmpty(fileIds)) {
			return true;
		}
		try {
			List<Future<Boolean>> futures = new ArrayList<>();
			// Execute async delete files
			for (String fileId : fileIds) {
				futures.add(googleDriveAsyncProvider.deleteFile(fileId));
			}
			while (!googleDriveAsyncProvider.isAllFutureDone(futures)) {
				// Wait until done
				// If all are not Done. Pause 100ms for next re-check
				Thread.sleep(AppConstant.WAIT_ASYNC_DONE_IN_MS);
			}
			// Get results
			for (Future<Boolean> future : futures) {
				if (BooleanUtils.isNotTrue(future.get())) {
					return false;
				}
			}
			return true;
		} catch (InterruptedException | IOException | ExecutionException e) {
			Thread.currentThread().interrupt();
			throw new GoogleDriveException("Can't delete files from Google drive ", e);
		}
	}

	/**
	 * Delete files if there are any error when upload files
	 */
	private void rollbackAsync(List<GoogleDriveFileResponse> responses) {
		try {
			if (CollectionUtils.isEmpty(responses)) {
				return;
			}
			List<String> fileIds = responses.stream().map(x -> x.getGoogleDriveFile().getId()).collect(Collectors.toList());
			this.deleteFilesAsync(fileIds);
		} catch (Exception e) {
			log.error("Can not rollback", e);
		}
	}

	/**
	 * Zip all files and perform download the zipped file
	 *
	 * @param request The {@link DownloadFileRequest}
	 * @param response The {@link HttpServletResponse}
	 * @throws IOException
	 */
	@Override
	public void zipAndDownloadAll(DownloadFileRequest request, HttpServletResponse response) throws IOException {
		// Servlet response
		response.setBufferSize(AppConstant.BUFFER_SIZE);
		response.setContentType(AppConstant.CONSUME_ZIP);
		response.setHeader(AppConstant.HEADER_CONTENT_DISPOSITION, request.getFileName());

		// Zip all files
		try (ServletOutputStream sos = response.getOutputStream();
             ZipOutputStream zos = new ZipOutputStream(sos);) {
			// Create root folder
			String zipParentFolderWithSep = FilenameUtils.getBaseName(request.getFileName()) + java.io.File.pathSeparator;
			zos.putNextEntry(new ZipEntry(zipParentFolderWithSep));

			// List file zipped
			Set<String> zippedFiles = new HashSet<>();

			List<ZipFileRequest> fileRequests = request.getFiles();
			if (BooleanUtils.isTrue(request.getGroupByAttachmentType())) {
				this.zipFilesWithGroupAttachmentType(fileRequests, zippedFiles, zipParentFolderWithSep,
						zos, BooleanUtils.isTrue(request.getZipFileTemp()));
			} else {
				this.zipFiles(fileRequests, zippedFiles, zipParentFolderWithSep, zos,
						BooleanUtils.isTrue(request.getZipFileTemp()));
			}

			zos.flush();
			sos.flush();
		}
		response.flushBuffer();
	}

	/**
	 * Zip files with group folder by attachment type
	 */
	private void zipFilesWithGroupAttachmentType(List<ZipFileRequest> fileRequests, Set<String> fileZippeds,
		 String zipParentFolderWithSeparate, ZipOutputStream zos, boolean zipTempFile) throws IOException {
		// Group files by attachment type
		Map<String, List<ZipFileRequest>> groupListFile = this.groupFileRequestByAttachmentTypeName(fileRequests);
		String entrySheetFolderZip;
		for (Entry<String, List<ZipFileRequest>> entry : groupListFile.entrySet()) {
			// Create sub-folder
			entrySheetFolderZip = zipParentFolderWithSeparate + entry.getKey() + java.io.File.pathSeparator;
			zos.putNextEntry(new ZipEntry(entrySheetFolderZip));

			// Zip files
			this.zipFiles(entry.getValue(), fileZippeds, entrySheetFolderZip, zos, zipTempFile);
			zos.closeEntry();
		}
	}

	/**
	 * Zip files.
	 * This method support download file both local files and Google drive files
	 */
	private void zipFiles(List<ZipFileRequest> fileRequests, Set<String> zippedFiles,
			String zipParentFolderWithSep, ZipOutputStream zos, boolean zipTempFile) throws IOException {
		if (CollectionUtils.isEmpty(fileRequests)) {
			return;
		}
		for (ZipFileRequest fileRequest : fileRequests) {
			if (StringUtils.isBlank(fileRequest.getAttachmentURL())) {
				continue;
			}

			// Create file
			String entrySheetFileZip = zipParentFolderWithSep + fileRequest.getAttachmentTitle();
			if (zippedFiles.contains(entrySheetFileZip)) {
				entrySheetFileZip = this.createUniqueFileName(zippedFiles, entrySheetFileZip);
			}
			zippedFiles.add(entrySheetFileZip);
			zos.putNextEntry(new ZipEntry(entrySheetFileZip));

			if (this.isLocalFile(fileRequest.getAttachmentURL())) {
				// Add content of local file to zip
				Path localFilePath = this.buildTempPathFromTempFileInUrl(fileRequest.getAttachmentURL());
				if (zipTempFile && localFilePath.toFile().exists()) {
					try (InputStream is = new FileInputStream(localFilePath.toFile())) {
						this.writeInputStreamToZipOutputStream(is, zos);
					}
				}
			} else {
				// Download file from google drive and add content of file to zip
				String googleDriveId = this.extractGoogleId(fileRequest.getAttachmentURL());
				try (InputStream is = drive.files().get(googleDriveId).executeMediaAsInputStream()) {
					this.writeInputStreamToZipOutputStream(is, zos);
				}
			}
		}
	}

	/**
	 * Get Temp Path of file from temp url
	 */
	private Path buildTempPathFromTempFileInUrl(String url) {
		String fileName = FilenameUtils.getName(url);
		Path fileStorageLocation = folderProvider.getTemporaryFolder();
		return fileStorageLocation.resolve(fileName);

	}

	/**
	 * Check the given URL is firl form temp folder
	 */
	private boolean isLocalFile(String url) {
		if (StringUtils.isNotBlank(url)) {
			return url.contains(ApiUrl.FILE_STORAGE);
		}
		return false;
	}

	/**
	 * Extract google drive id from the given url
	 * 
	 * @param url The request url
	 * @return The value of query param {AppConstant.GOOGLE_DRIVE_QUERY_PARAM_ID}
	 */
	private String extractGoogleId(String url) {
		return UrlUtil.getValueOfQueryParam(AppConstant.GOOGLE_DRIVE_QUERY_PARAM_ID, url);
	}

	/**
	 * Create unique file name, if the given file name is exists in the given list,
	 * it will return the concat of file name and increase number
	 */
	private String createUniqueFileName(Set<String> fileNames, final String baseFileName) {
		int increase = 1;
		String newFileName = baseFileName;
		String fileNameWithoutExtension = FilenameUtils.getBaseName(baseFileName);
		String extension = FilenameUtils.getExtension(baseFileName);
		String basePath = FilenameUtils.getFullPath(baseFileName);
		while (fileNames.contains(newFileName)) {
			newFileName = basePath + fileNameWithoutExtension + " (" + increase + ")"
					+ FilenameUtils.EXTENSION_SEPARATOR + extension;
			increase++;
		}
		return newFileName;
	}

	/**
	 * Group list attachment by attachment type name
	 */
	private Map<String, List<ZipFileRequest>> groupFileRequestByAttachmentTypeName(List<ZipFileRequest> fileRequests) {
		if (CollectionUtils.isEmpty(fileRequests)) {
			return new HashMap<>();
		}
		return fileRequests.stream()
				.collect(Collectors.groupingBy(ZipFileRequest::getAttachmentName, Collectors.toList()));
	}

	/**
	 * Write input stream to zip output stream
	 */
	private void writeInputStreamToZipOutputStream(InputStream is, ZipOutputStream zos) throws IOException {
		byte[] bytes = new byte[AppConstant.BUFFER_SIZE];
		int bytesRead;
		try (BufferedInputStream bis = new BufferedInputStream(is)) {
			while ((bytesRead = bis.read(bytes)) != -1) {
				zos.write(bytes, 0, bytesRead);
				zos.flush();
			}
		}
	}
}
