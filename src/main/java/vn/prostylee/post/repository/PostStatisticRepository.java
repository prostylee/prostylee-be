package vn.prostylee.post.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.post.dto.response.PostStatisticResponse;
import vn.prostylee.post.entity.PostStatistic;
import vn.prostylee.product.entity.ProductStatistic;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostStatisticRepository extends BaseRepository<PostStatistic, Long> {

    @Query("SELECT e FROM #{#entityName} e where e.id in :ids")
    List<PostStatistic> findByPostIds(@Param("ids") Collection<Long> ids);
}
