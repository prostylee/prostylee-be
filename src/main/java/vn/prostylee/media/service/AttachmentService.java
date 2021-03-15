package vn.prostylee.media.service;

import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.request.MediaFileRequest;
import vn.prostylee.media.dto.response.AttachmentResponse;
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

    Attachment saveAttachmentByNameAndPath(String name, String path);



    /**
     * Store and generate the path to attachment table
     *
     * @param mediaFileRequests The {@link MediaFileRequest}
     * @return total size of saved entity
     */
    int storeFiles(List<MediaFileRequest> mediaFileRequests);

    Attachment getById(Long id);

    List<Attachment> getByIds(List<Long> ids);

    boolean deleteAttachmentsByIdIn(List<Long> ids);
}
