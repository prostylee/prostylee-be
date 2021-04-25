package vn.prostylee.product.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Category generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends AuditEntity {

	private static final long serialVersionUID = 1L;

	public Category(Long id){
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "parent_id")
	private Long parentId;

	@Column(name = "attachment_id")
	private Long attachmentId;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "description", length = 4096)
	private String description;

	@Column(name = "icon", length = 512)
	private String icon;

	@Column(name = "order")
	private Integer order;

	@Generated(GenerationTime.INSERT)
	@Column(name = "language_code")
	private String languageCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

	@Generated(GenerationTime.INSERT)
	@Column(name = "active")
	private Boolean active;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "category_attribute", joinColumns = {
			@JoinColumn(name = "category_id", nullable = false, updatable = false)}, inverseJoinColumns = {
			@JoinColumn(name = "attribute_id", nullable = false, updatable = false)
	})
	private Set<Attribute> attributes;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category")
	private Set<Product> products;


}
