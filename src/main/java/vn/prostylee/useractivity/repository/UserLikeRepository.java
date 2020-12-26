package vn.prostylee.useractivity.repository;


import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserLike;

/**
 * Repository for domain model class User Like.
 * @see UserLike ;
 * @author prostylee
 */
@Repository
public interface UserLikeRepository extends BaseRepository<UserLike, Long> {

}

