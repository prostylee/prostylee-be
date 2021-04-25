package vn.prostylee.product.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Attribute generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "attribute")
public class Attribute extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "attribute_seq", sequenceName = "attribute_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attribute_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "category_attribute", joinColumns = {
			@JoinColumn(name = "attribute_id", nullable = false, updatable = false)}, inverseJoinColumns = {
			@JoinColumn(name = "category_id", nullable = false, updatable = false)
	})
	private Set<Category> categories;

	@Column(name = "key", length = 512)
	private String key;

	@Column(name = "order")
	private Integer order;

	@Column(name = "label", length = 128)
	private String label;

	@Column(name = "description", length = 512)
	private String description;

	@Column(name = "type")
	private Integer type;

	@Generated(GenerationTime.INSERT)
	@Column(name = "language_code")
	private String languageCode;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "attribute")
	private Set<AttributeOption> attributeOptions;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "attribute")
	private Set<ProductAttribute> productAttributes;

}
