package vn.prostylee.auth.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

/**
 * User generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user", catalog = "prostylee", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "username", length = 128)
	private String username;

	@Column(name = "password", length = 128)
	private String password;

	@Column(name = "full_name", length = 512)
	private String fullName;

	@Column(name = "gender")
	private Character gender;

	@Column(name = "phone_number", length = 32)
	private String phoneNumber;

	@Column(name = "email", unique = true, length = 512)
	private String email;

	@Column(name = "location_id", length = 512)
	private String locationId;

	@Column(name = "allow_notification")
	private Boolean allowNotification;

	@Column(name = "active")
	private Boolean active;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at", length = 19)
	private Date deletedAt;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_role", catalog = "prostylee", joinColumns = {
			@JoinColumn(name = "user_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", nullable = false, updatable = false) })
	private Set<Role> roles;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@OneToMany(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "user")
	private Set<UserLinkAccount> userLinkAccounts;

	@Column(name = "avatar")
	private String avatar;
}
