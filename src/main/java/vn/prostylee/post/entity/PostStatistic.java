package vn.prostylee.post.entity;
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
@Table(name = "post_statistic")
public class PostStatistic extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "number_of_like")
    private Long numberOfLike;

    @Column(name = "number_of_comment")
    private Long numberOfComment;


}