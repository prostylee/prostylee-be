package vn.prostylee.post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.prostylee.comment.entity.Comment;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

/**
 * PostImage entity
 * @author prostylee
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Table(name = "post_image")
public class PostImage extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "post_image_seq", sequenceName = "post_image_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_image_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * @see <a href="https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue">Fix problem Infinite recursion</a>
     * By using @JsonIgnore
     */
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "attachment_id")
    private Long attachmentId;

    @Column(name = "order")
    private Integer order;
}
