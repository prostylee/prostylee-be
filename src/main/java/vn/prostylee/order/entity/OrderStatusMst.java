package vn.prostylee.order.entity;

import lombok.*;
import org.hibernate.annotations.GenerationTime;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "order_status")
public class OrderStatusMst extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "order_status_seq", sequenceName = "order_status_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_status_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

	@org.hibernate.annotations.Generated(GenerationTime.INSERT)
	@Column(name = "active")
	private Boolean active;

	@Column(name = "description", length = 4096)
	private String description;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "act_code")
	private int actCode;

	@Column(name = "step")
	private Integer step;

	@org.hibernate.annotations.Generated(GenerationTime.INSERT)
	@Column(name = "language_code")
	private String languageCode;

	@Column(name = "group", length = 64)
	private String group;

}
