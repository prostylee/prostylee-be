package vn.prostylee.ads.entity;

import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "advertisement_campaign")
public class AdvertisementCampaign extends AuditEntity {

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

    @Column(name = "feature_image")
    private Long featureImage;

    @Column(name = "position", length = 128)
    private String position;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "from_date")
    private Date fromDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "to_date")
    private Date toDate;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "target_type", length = 512)
    private String targetType;

    @Column(name = "target_from_age")
    private Integer targetFromAge;

    @Column(name = "target_to_age")
    private Integer targetToAge;

    @Column(name = "target_location_id")
    private Long targetLocationId;

    @Column(name = "target_user_follower")
    private Boolean targetUserFollower;

    @Column(name = "target_user_like")
    private Boolean targetUserLike;

}
