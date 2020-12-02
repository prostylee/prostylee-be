package vn.prostylee.useractivity.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.prostylee.core.entity.AuditEntity;

/**
 * UserTracking generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_tracking", catalog = "prostylee")
public class UserTracking extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "store_id")
	private Long storeId;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "search_keyword", length = 512)
	private String searchKeyword;

}
