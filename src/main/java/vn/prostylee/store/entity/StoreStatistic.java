package vn.prostylee.store.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * StoreStatistic generated by hbm2java
 * @author prostylee
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "store_statistic")
public class StoreStatistic extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "store_statistic_seq", sequenceName = "store_statistic_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_statistic_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "number_of_product")
    private Long numberOfProduct;

    @Column(name = "number_of_like")
    private Long numberOfLike;

    @Column(name = "number_of_comment")
    private Long numberOfComment;

    @Column(name = "number_of_follower")
    private Long numberOfFollower;

    @Column(name = "number_of_following")
    private Long numberOfFollowing;

}
