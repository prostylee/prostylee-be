package vn.prostylee.product.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Language extends AuditEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "language_seq", sequenceName = "language_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "language_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column( name = "code", length = 20)
    private String code;

    @Column(name = "name", length = 512)
    private String name;

    @Column(name = "status", length = 512)
    private boolean status;
}
