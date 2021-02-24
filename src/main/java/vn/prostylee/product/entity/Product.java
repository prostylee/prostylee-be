package vn.prostylee.product.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;
import vn.prostylee.product.constant.ProductStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Product generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
public class Product extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "description", length = 512)
	private String description;

	@Column(name = "status")
	private Integer status;

	@Column(name = "location_id", length = 512)
	private Long locationId;

	@Column(name = "store_id")
	private Long storeId;

	@Column(name = "price", precision = 22, scale = 0)
	private Double price;

	@Column(name = "price_sale", precision = 22, scale = 0)
	private Double priceSale;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "published_date", length = 19)
	private Date publishedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
	private Set<ProductPrice> productPrices;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
	private Set<ProductImage> productImages;

}
