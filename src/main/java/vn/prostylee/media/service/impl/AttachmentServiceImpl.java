package vn.prostylee.media.service.impl;

import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.repository.AttachmentRepository;
import vn.prostylee.media.service.AttachmentService;

import java.net.URL;
import java.util.List;

/**
 * Attachment Service
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

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

}
