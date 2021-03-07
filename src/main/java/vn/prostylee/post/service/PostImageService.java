package vn.prostylee.post.service;

import vn.prostylee.post.dto.request.PostImageRequest;
import vn.prostylee.post.entity.Post;
import vn.prostylee.post.entity.PostImage;

import java.util.List;
import java.util.Set;

public interface PostImageService {
    Set<PostImage> handlePostImages(List<PostImageRequest> postImageRequests, Post postEntity);
}
