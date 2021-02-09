package vn.prostylee.media.service;

import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.entity.Attachment;

import java.net.URL;
import java.util.List;

public interface AttachmentService {

    /**
     * Save attachment based on uploaded file
     *
     * @param fileUrl file's url
     * @param file file to save
     * @return The attachment
     */
    Attachment saveAttachmentByUploadFile(URL fileUrl, MultipartFile file);

}
