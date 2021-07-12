package vn.prostylee.post.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.post.entity.PostImage;

import java.util.List;

@Repository
public interface PostImageRepository extends BaseRepository<PostImage, Long> {

    @Query(value = "SELECT p.attachment_id " +
            "FROM post_image p " +
            "WHERE p.post_id = :postId " +
            "ORDER BY p.order ", nativeQuery = true)
    List<Long> getAttachmentIdByPostId(@Param("postId") Long postId);
}
