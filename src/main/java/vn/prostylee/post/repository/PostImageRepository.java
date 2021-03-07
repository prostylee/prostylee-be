package vn.prostylee.post.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.post.entity.PostImage;

@Repository
public interface PostImageRepository extends BaseRepository<PostImage, Long> {
}
