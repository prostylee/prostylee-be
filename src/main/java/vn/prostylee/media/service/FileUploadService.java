package vn.prostylee.media.service;

import com.google.api.services.drive.model.File;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.response.AttachmentResponse;

import java.util.List;

public interface FileUploadService {

    /**
     * Upload files to the default folder id
     *
     * @param files The {@link MultipartFile}
     * @return The list of {@link File} after uploaded
     */
    List<AttachmentResponse> uploadFiles(MultipartFile... files);

    /**
     * Upload files to the given google folder id.
     * This method will execute uploading in async mode.
     *
     * @param files The {@link MultipartFile}
     * @param folderId The folder id will stored the given files
     * @return The list of {@link File} after uploaded
     */
    List<AttachmentResponse> uploadFiles(String folderId, MultipartFile... files);

    /**
     * Perform delete multiple files
     *
     * @param fileIds The files will be deleted
     * @return true if deleted successfully, otherwise false
     */
    boolean deleteFiles(String... fileIds);

}
