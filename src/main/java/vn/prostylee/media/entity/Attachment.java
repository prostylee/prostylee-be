package vn.prostylee.media.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * Attachment generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "attachment")
public class Attachment extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "attachment_seq", sequenceName = "attachment_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name", length = 512)
	private String name;

	@Column(name = "type", length = 512)
	private String type;

	@Column(name = "path", length = 2048)
	private String path;

	@Column(name = "display_name", length = 512)
	private String displayName;

	@Column(name = "thumbnail", length = 2048)
	private String thumbnail;

	@Column(name = "size_in_kb")
	private Long sizeInKb;

}
