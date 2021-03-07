package vn.prostylee.post.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;
import java.util.Set;
/**
 * Post entity
 * @author prostylee
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "post")
public class Post extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "post_seq", sequenceName = "post_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "store_id")
    private Long storeId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private Set<PostImage> postImages;
}
