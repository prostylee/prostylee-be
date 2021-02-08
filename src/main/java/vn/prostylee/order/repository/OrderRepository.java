package vn.prostylee.order.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.order.entity.Order;

import java.util.List;

/**
 * Repository for domain model class Order.
 * @see Order;
 * @author prostylee
 */
@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {

    List<Order> findAllByStatus(Integer statusId);
}
