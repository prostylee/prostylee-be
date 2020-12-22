package vn.prostylee.app.entity;
// Generated Jun 1, 2020, 11:38:05 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.core.entity.AuditEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Contact generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "metadata")
public class AppMetadata extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "metadata_seq", sequenceName = "metadata_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadata_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "content", length = 65556)
	private String content;

	@Column(name = "code", length = 128)
	private String code;

}
