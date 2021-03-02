package vn.prostylee.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.service.AttachmentService;
import vn.prostylee.post.dto.request.PostImageRequest;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;
import vn.prostylee.post.service.PostImageService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final AttachmentService attachmentService;

    @Override
    public Set<PostImage> handlePostImages(List<PostImageRequest> postImageRequests, Post postEntity) {
        return IntStream.range(0, postImageRequests.size())
                .mapToObj(index -> handleProductImage(postEntity, postImageRequests, index)
                ).collect(Collectors.toSet());
    }

    private PostImage handleProductImage(Post postEntity, List<PostImageRequest> postImageRequests, int index) {
        Attachment attachment = this.saveAttachment(postImageRequests.get(index));
        return buildProductImage(postEntity, attachment.getId(), index + 1);
    }

    private PostImage buildProductImage(Post postEntity, Long id, Integer orderIndex) {
        return PostImage.builder().attachmentId(id).post(postEntity)
                .order(orderIndex)
                .build();
    }

    private Attachment saveAttachment(PostImageRequest postImageRequest){
        return attachmentService.saveAttachmentByNameAndPath(postImageRequest.getName(), postImageRequest.getPath());
    }





}
