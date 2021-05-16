package vn.prostylee.notification.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "push_notification_template")
public class PushNotificationTemplate extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "push_notification_template_seq", sequenceName = "push_notification_template_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_notification_template_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "title", length = 512)
	private String title;

	@Column(name = "content", length = 4096)
	private String content;

	@Column(name = "type", length = 128)
	private String type;

}
