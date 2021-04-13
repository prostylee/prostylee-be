package vn.prostylee.media.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.media.dto.request.MediaFileRequest;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.repository.AttachmentRepository;
import vn.prostylee.media.service.AttachmentService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Attachment Service
 */
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Override
    public Attachment saveAttachmentByUploadFile(URL fileUrl, MultipartFile file) {
        if (fileUrl == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setType(file.getContentType());
        attachment.setPath(fileUrl.toString());
        attachment.setName(fileUrl.getFile().replaceAll("/", ""));
        attachment.setDisplayName(file.getOriginalFilename());
        attachment.setSizeInKb(file.getSize() / 1024);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment saveAttachmentByNameAndPath(String name, String path) {
        Attachment attachment = new Attachment();
        attachment.setPath(path);
        attachment.setName(name);
        attachment.setDisplayName(name);
        return attachmentRepository.save(attachment);
    }

    /**
     * Give the MediaFileRequest save to Attachment table
     * @param mediaFileRequests The {@link MediaFileRequest}
     * @return size of saved Entity.
     */
    @Override
    public int storeFiles(List<MediaFileRequest> mediaFileRequests) {
        List<Attachment> entities = new ArrayList<>();
        mediaFileRequests.forEach(mediaFileRequest -> {
            String name =  mediaFileRequest.getName();
            entities.add(Attachment.builder()
                    .name(name).displayName(name)
                    .path(mediaFileRequest.getPath())
                    .build());
        });
        return attachmentRepository.saveAll(entities).size();
    }

    @Override
    public Attachment getById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post is not found with id [" + id + "]"));
    }

    @Override
    public List<Attachment> getByIds(List<Long> ids) {
        return attachmentRepository.findAllById(ids);
    }

    @Override
    public boolean deleteAttachmentsByIdIn(List<Long> ids) {
        return attachmentRepository.deleteAttachmentsByIdIn(ids);
    }

}
