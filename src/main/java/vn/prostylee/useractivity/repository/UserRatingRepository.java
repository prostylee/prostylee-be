package vn.prostylee.useractivity.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserRating;

/**
 * Repository for domain model class User Rating.
 * @see UserRating ;
 * @author prostylee
 */
@Repository
public interface UserRatingRepository extends BaseRepository<UserRating, Long> {

}
