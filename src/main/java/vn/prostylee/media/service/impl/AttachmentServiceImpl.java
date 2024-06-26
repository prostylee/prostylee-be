package vn.prostylee.media.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.repository.AttachmentRepository;
import vn.prostylee.media.service.AttachmentService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        attachment.setName(fileUrl.getFile().replace("/", ""));
        attachment.setDisplayName(file.getOriginalFilename());
        attachment.setSizeInKb(file.getSize() / 1024);
        return attachmentRepository.save(attachment);
    }

    @Override
    public Attachment saveAttachmentByNameAndPath(String name, String path) {
        MediaRequest request = MediaRequest.builder()
                .name(name)
                .path(path)
                .build();
        return save(request);
    }

    @Override
    public Attachment save(MediaRequest mediaRequest) {
        Attachment attachment = convert(mediaRequest);
        return attachmentRepository.save(attachment);
    }

    @Override
    public List<Attachment> saveAll(List<MediaRequest> mediaRequests) {
        List<Attachment> attachments = Optional.ofNullable(mediaRequests)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return attachmentRepository.saveAll(attachments);
    }

    private Attachment convert(MediaRequest mediaRequest) {
        return Attachment.builder()
                .path(mediaRequest.getPath())
                .name(mediaRequest.getName())
                .displayName(mediaRequest.getName())
                .build();
    }

    /**
     * Give the MediaRequest save to Attachment table
     * @param mediaRequests The {@link MediaRequest}
     * @return size of saved Entity.
     */
    @Override
    public int storeFiles(List<MediaRequest> mediaRequests) {
        List<Attachment> entities = new ArrayList<>();
        mediaRequests.forEach(mediaRequest -> {
            String name =  mediaRequest.getName();
            entities.add(Attachment.builder()
                    .name(name).displayName(name)
                    .path(mediaRequest.getPath())
                    .build());
        });
        return attachmentRepository.saveAll(entities).size();
    }

    @Cacheable(value = CachingKey.ATTACHMENTS, key = "#id")
    @Override
    public Attachment getById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment is not found with id [" + id + "]"));
    }

    @Cacheable(value = CachingKey.ATTACHMENTS, key = "#ids")
    @Override
    public List<Attachment> getByIds(List<Long> ids) {
        return attachmentRepository.findAllById(ids);
    }

    @Override
    public boolean deleteAttachmentsByIdIn(List<Long> ids) {
        return attachmentRepository.deleteAttachmentsByIdIn(ids);
    }

}
