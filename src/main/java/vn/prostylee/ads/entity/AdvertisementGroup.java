package vn.prostylee.ads.entity;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "advertisement_group")
public class AdvertisementGroup extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "advertisement_group_seq", sequenceName = "advertisement_group_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advertisement_group_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "position", length = 128)
    private String position;

    @Column(name = "active")
    private Boolean active;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
    private Set<AdvertisementBanner> advertisementBanners;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group")
    private Set<AdvertisementCampaign> advertisementCampaigns;

}
