package vn.prostylee.notification.entity;
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
 * PushNotificationToken generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "push_notification_token", catalog = "prostylee")
public class PushNotificationToken extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "store_id")
	private Long storeId;

	@Column(name = "token", length = 512)
	private String token;

	@Column(name = "device_name", length = 256)
	private String deviceName;

	@Column(name = "device_id", length = 256)
	private String deviceId;

	@Column(name = "software", length = 256)
	private String software;

	@Column(name = "os_version", length = 256)
	private String osVersion;

	@Column(name = "os_name", length = 512)
	private String osName;

	@Column(name = "brand", length = 256)
	private String brand;

	@Column(name = "manufacturer", length = 256)
	private String manufacturer;

	@Column(name = "model_name", length = 256)
	private String modelName;

}
