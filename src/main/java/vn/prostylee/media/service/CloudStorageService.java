package vn.prostylee.media.service;

import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.request.DownloadFileRequest;
import vn.prostylee.media.dto.request.UploadFileRequest;
import vn.prostylee.media.dto.response.GoogleDriveFileResponse;
import vn.prostylee.media.exception.GoogleDriveException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CloudStorageService {

    /**
     * Get list folders and files in the default Google folder id ${app.google.folderId}
     *
     * @return The list of {@link File}
     */
    List<File> getFiles();

    /**
     * Get list folders and files in the given Google folder id
     *
     * @return The list of {@link File}
     */
    List<File> getFilesInFolder(String folderId);

    /**
     * Upload files to the default Google folder id ${app.google.folderId}
     *
     * @param files The {@link MultipartFile}
     * @return The list of {@link File} after uploaded
     */
    List<File> uploadFiles(MultipartFile... files);

    /**
     * Upload files to the given google folder id.
     * This method will execute uploading in async mode.
     *
     * @param files The {@link MultipartFile}
     * @param folderId The google folder id will stored the given files
     * @return The list of {@link File} after uploaded
     */
    List<File> uploadFiles(String folderId, MultipartFile... files);

    /**
     * Download file of the given google file id
     *
     * @param fileId The google file id
     * @return The byte array of downloaded file
     */
    byte[] downloadFile(String fileId);

    /**
     * Perform delete a Google file
     *
     * @param fileId The Google file id will be deleted
     * @return true if deleted successfully, otherwise false
     */
    boolean deleteFile(String fileId);

    /**
     * Get a File with the given Google file id
     *
     * @param fileId The Google file id
     * @return The {@link File}
     */
    File getFile(String fileId);

    /**
     * Create folder on Google drive
     *
     * @param folderName The folder name will be created
     * @return The folder {@link File} after created or null if error while creating folder
     */
    File createFolder(String folderName);

    /**
     * Create folder on Google drive
     *
     * @param folderName The folder name will be created
     * @param parentId The parent Google folder id. If the given id is null, it will be created in default setting folder
     * @return The folder {@link File} after created or null if error while creating folder
     */
    File createFolder(String folderName, String parentId);

    /**
     * Perform async storage file to Google drive
     *
     * @param requests The request files need to upload
     * @param folderId The Google folder id will contains the uploaded files
     * @return The {@link GoogleDriveFileResponse}
     *
     * @throws GoogleDriveException if can not upload files to google drive
     */
    List<GoogleDriveFileResponse> uploadFilesAsync(String folderId, UploadFileRequest... requests);

    /**
     * Perform delete files from Google drive
     *
     * @param  fileIds The list of Google file ids will be deleted
     *
     * @throws GoogleDriveException if can not delete files from google drive
     */
    boolean deleteFilesAsync(List<String> fileIds);
    
    /**
     * Zip all files and perform download the zipped file
     *
     * @param request The {@link DownloadFileRequest}
     * @param response The {@link HttpServletResponse}
     * @throws IOException
     */
    void zipAndDownloadAll(DownloadFileRequest request, HttpServletResponse response) throws IOException;
}
