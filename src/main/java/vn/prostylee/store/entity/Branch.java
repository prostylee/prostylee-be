package vn.prostylee.store.entity;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "branch")
public class Branch extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "branch_seq", sequenceName = "branch_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "description", length = 4096)
	private String description;

	@Column(name = "location_id")
	private Long locationId;

	@Builder.Default
	@Column(name = "active", columnDefinition = "boolean default false")
	private Boolean active = true;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

}
