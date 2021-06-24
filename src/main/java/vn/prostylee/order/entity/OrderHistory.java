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
@Table(name = "order_history")
public class OrderHistory extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "order_history_seq", sequenceName = "order_history_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_history_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

	@Column(name = "status_name", length = 512)
	private String statusName;

	@Column(name = "act_code")
	private int actCode;

	@Column(name = "step")
	private Integer step;

	@Column(name = "completed_status")
	private Integer completedStatus;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Column(name = "status_id", nullable = false)
	private Long statusId;

}
