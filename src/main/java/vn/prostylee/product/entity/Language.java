package vn.prostylee.product.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "language")
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

    @Column(name = "status")
    private boolean status;
}
