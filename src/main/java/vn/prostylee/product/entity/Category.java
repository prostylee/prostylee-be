package vn.prostylee.product.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import vn.prostylee.core.entity.AuditEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Category generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "description", length = 4096)
	private String description;

	@Column(name = "icon", length = 512)
	private String icon;

	@Column(name = "order")
	private Integer order;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

	@Column(name = "active")
	private Boolean active;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category")
	private Set<Attribute> attributes;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category")
	private Set<Product> products;

}
