package vn.prostylee.product.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import java.util.Set;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

/**
 * Brand generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "brand")
public class Brand extends AuditEntity {

	private static final long serialVersionUID = 1L;

	public Brand(Long id){
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "brand_seq", sequenceName = "brand_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "description", length = 4096)
	private String description;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "brand")
	private Set<Product> products;

	@Column(name = "icon", length = 512)
	private String icon;
}
