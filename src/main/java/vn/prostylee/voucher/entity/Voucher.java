package vn.prostylee.voucher.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * ShippingAddress generated by hbm2java
 *
 * @author prostylee
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "voucher")
public class Voucher extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "voucher_seq", sequenceName = "voucher_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voucher_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 512)
    private String name;

}
