package vn.prostylee.story.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Table(name = "story_image")
public class StoryImage extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "story_image_seq", sequenceName = "story_image_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "story_image_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Column(name = "attachment_id")
    private Long attachmentId;

    @Column(name = "order")
    private Integer order;
}
