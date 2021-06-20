package vn.prostylee.product.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * ProductStatistic generated by hbm2java
 * @author prostylee
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_statistic")
public class ProductStatistic extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "product_statistic_seq", sequenceName = "product_statistic_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_statistic_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "number_of_sold")
    private Long numberOfSold;

    @Column(name = "number_of_like")
    private Long numberOfLike;

    @Column(name = "number_of_comment")
    private Long numberOfComment;

    @Column(name = "result_of_rating")
    private Double resultOfRating;

    @Column(name = "number_of_review")
    private Long numberOfReview;

}
