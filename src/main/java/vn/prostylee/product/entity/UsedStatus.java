package vn.prostylee.product.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "used_status")
@Entity
public class UsedStatus extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "used_status_seq", sequenceName = "used_status_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "used_status_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name")
    private String name;

}
