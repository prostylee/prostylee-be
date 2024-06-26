package vn.prostylee.auth.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * UserLinkAccount generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_link_account")
public class UserLinkAccount extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "user_link_account_seq", sequenceName = "user_link_account_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_link_account_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "provider_name", length = 45)
	private String providerName;

	@Column(name = "provider_id", length = 45)
	private String providerId; //id user

}
