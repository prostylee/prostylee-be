package vn.prostylee.voucher.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.voucher.entity.VoucherUser;

/**
 * Repository for domain model class VoucherUser.
 * @see VoucherUser ;
 * @author prostylee
 */
@Repository
public interface VoucherUserRepository extends BaseRepository<VoucherUser, Long> {

    VoucherUser findTopByVoucherId(Long voucherId);

    @Query("SELECT COUNT(e) FROM #{#entityName} e " +
            "WHERE e.voucherId = :voucherId AND (:userId IS NULL OR e.createdBy = :userId)")
    int countVoucher(@Param("voucherId") Long voucherId, @Param("userId") Long userId);

}
