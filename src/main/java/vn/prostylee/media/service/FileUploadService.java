package vn.prostylee.media.service;

import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.response.AttachmentResponse;

import java.util.List;

public interface FileUploadService {

    /**
     * Get list file's url
     *
     * @param  fileIds The list of file ids will be get
     * @return The list file's url
     */
    List<String> getFileUrls(List<Long> fileIds);

    /**
     * Get image url
     *
     * @param  id is a attachmentId will be get
     * @param width width
     * @param height height
     * @return image url
     */
    String getImageUrl(Long id, int width, int height);

    /**
     * Get list image's url
     *
     * @param  fileIds The list of image ids will be get
     * @param width width
     * @param height height
     * @return The list image's url
     */
    List<String> getImageUrls(List<Long> fileIds, int width, int height);

    /**
     * Upload files to the default folder id
     *
     * @param files The {@link MultipartFile}
     * @return The list of {@link File} after uploaded
     */
    List<AttachmentResponse> uploadFiles(List<MultipartFile> files);

    /**
     * Upload files to the given folder id.
     * This method will execute uploading in async mode.
     *
     * @param folderId The folder id will stored the given files
     * @param files The {@link MultipartFile}
     * @return The list of {@link File} after uploaded
     */
    List<AttachmentResponse> uploadFiles(String folderId, List<MultipartFile> files);

    /**
     * Perform delete multiple files
     *
     * @param fileIds The files will be deleted
     * @return true if deleted successfully, otherwise false
     */
    boolean deleteFiles(List<Long> fileIds);

}
