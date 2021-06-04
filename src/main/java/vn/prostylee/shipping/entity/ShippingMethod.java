package vn.prostylee.shipping.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * ShippingMethod generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "shipping_method")
public class ShippingMethod extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "shipping_method_seq", sequenceName = "shipping_method_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipping_method_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "description", length = 512)
	private String description;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "shippingMethod")
	private Set<ShippingProvider> shippingProviders;
}
