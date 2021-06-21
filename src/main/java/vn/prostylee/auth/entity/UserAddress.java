package vn.prostylee.auth.entity;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_address")
public class UserAddress extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "user_address_seq", sequenceName = "user_address_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_address_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "city_code", length = 20, nullable = false)
    private String cityCode;

    @Column(name = "district_code", length = 20, nullable = false)
    private String districtCode;

    @Column(name = "ward_code", length = 20, nullable = false)
    private String wardCode;

    @Column(name = "address", length = 512, nullable = false)
    private String address;

    @Column(name = "full_address", length = 512, nullable = false)
    private String fullAddress;

    @Column(name = "priority")
    private Boolean priority;

    @Column(name = "contact_name", length = 128, nullable = false)
    private String contactName;

    @Column(name = "contact_phone", length = 32, nullable = false)
    private String contactPhone;

}
