package vn.prostylee.location.entity;
// Generated Nov 28, 2020, 9:45:59 PM by Hibernate Tools 5.2.12.Final

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * Location generated by hbm2java
 * @author prostylee
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "location")
public class Location extends AuditEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "location_seq", sequenceName = "location_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "address", length = 512)
	private String address;

	@Column(name = "latitude", length = 512)
	private Double latitude;

	@Column(name = "longitude", length = 512)
	private Double longitude;

	@Column(name = "state", length = 512)
	private String state;

	@Column(name = "city", length = 512)
	private String city;

	@Column(name = "country", length = 512)
	private String country;

	@Column(name = "zipcode", length = 512)
	private String zipcode;

	@Column(name = "target_type", length = 64)
	@Enumerated(EnumType.STRING)
	private TargetType targetType;

}
