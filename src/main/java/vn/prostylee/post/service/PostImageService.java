package vn.prostylee.post.service;

import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;

import java.util.List;
import java.util.Set;

public interface PostImageService {

    Set<PostImage> saveImages(List<MediaRequest> images, Post postEntity);
}
