package vn.prostylee.useractivity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * User Wish List entity
 * @author prostylee
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_wish_list")
public class UserWishList extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "user_wish_list_seq", sequenceName = "user_wish_list_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_wish_list_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at", length = 19)
    private Date deletedAt;

    @Column(name = "product_id")
    private Long productId;
}
