package vn.prostylee.useractivity.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.useractivity.entity.UserWishList;

/**
 * Repository for domain model class UserWishList.
 * @see UserWishList ;
 * @author prostylee
 */
@Repository
public interface UserWishListRepository extends BaseRepository<UserWishList, Long>{
}
