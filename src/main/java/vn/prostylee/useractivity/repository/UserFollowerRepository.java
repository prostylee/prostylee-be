package vn.prostylee.useractivity.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserFollower;

/**
 * Repository for domain model class User Follower.
 * @see UserFollower;
 * @author prostylee
 */
@Repository
public interface UserFollowerRepository extends BaseRepository<UserFollower, Long> {

}
