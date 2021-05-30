package vn.prostylee.store.entity;


import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "store_banner")
public class StoreBanner extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "store_banner_seq", sequenceName = "store_banner_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_banner_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "banner_image")
    private Long bannerImage;

    @Column(name = "link", length = 2048)
    private String link;

    @Column(name = "order")
    private Integer order;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

}

