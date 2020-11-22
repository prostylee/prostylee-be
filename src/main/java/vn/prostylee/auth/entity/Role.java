package vn.prostylee.auth.entity;
// Generated Jun 1, 2020, 11:38:05 PM by Hibernate Tools 5.2.12.Final

import com.fasterxml.jackson.annotation.JsonIgnore;
import vn.prostylee.core.entity.AuditEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Role generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Role extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 45)
	private String name;

	@Column(name = "code", unique = true, nullable = false, length = 45)
	private String code;

	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "feature_role", joinColumns = {
			@JoinColumn(name = "role_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "feature_id", nullable = false, updatable = false) })
	private Set<Feature> features;


}
