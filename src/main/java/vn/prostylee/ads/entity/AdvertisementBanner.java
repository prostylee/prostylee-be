package vn.prostylee.ads.entity;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "advertisement_banner")
public class AdvertisementBanner extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "advertisement_banner_seq", sequenceName = "advertisement_banner_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advertisement_banner_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "description", length = 4096)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_group_id", nullable = false)
    private AdvertisementGroup group;

    @Column(name = "banner_image")
    private Long bannerImage;

    @Column(name = "link", length = 2048)
    private String link;

    @Column(name = "order")
    private Integer order;

}
