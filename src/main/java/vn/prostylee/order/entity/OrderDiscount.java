package vn.prostylee.order.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * OrderDiscount generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_discount")
public class OrderDiscount extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "order_discount_seq", sequenceName = "order_discount_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_discount_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "voucher_id")
	private Long voucherId;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "description", length = 512)
	private String description;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
}
