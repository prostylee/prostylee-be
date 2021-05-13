package vn.prostylee.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;
import vn.prostylee.post.service.PostImageService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final AttachmentService attachmentService;

    @Override
    public Set<PostImage> saveImages(List<MediaRequest> postImageRequests, Post postEntity) {
        return attachmentService.saveAll(postImageRequests)
                .stream()
                .map(attachment -> buildProductImage(postEntity, attachment.getId()))
                .collect(Collectors.toSet());
    }

    private PostImage buildProductImage(Post postEntity, Long id) {
        return PostImage.builder()
                .attachmentId(id)
                .post(postEntity)
                .order(0)
                .build();
    }
}
