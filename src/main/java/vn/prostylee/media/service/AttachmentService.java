package vn.prostylee.media.service;

import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.request.MediaRequest;
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

    Attachment save(MediaRequest mediaRequest);

    List<Attachment> saveAll(List<MediaRequest> mediaRequests);

    /**
     * Store and generate the path to attachment table
     *
     * @param mediaRequests The {@link MediaRequest}
     * @return total size of saved entity
     */
    int storeFiles(List<MediaRequest> mediaRequests);

    Attachment getById(Long id);

    List<Attachment> getByIds(List<Long> ids);

    boolean deleteAttachmentsByIdIn(List<Long> ids);
}
