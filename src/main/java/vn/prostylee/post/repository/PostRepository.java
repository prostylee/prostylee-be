package vn.prostylee.post.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.post.entity.Post;

@Repository
public interface PostRepository extends BaseRepository<Post,Long> {
}
