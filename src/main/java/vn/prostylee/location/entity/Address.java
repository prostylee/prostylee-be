package vn.prostylee.location.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Address extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "address_seq", sequenceName = "address_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "parent_code", length = 20)
    private String parentCode;

    @Column(name = "name", nullable = false, length = 512)
    private String name;

    @Column(name = "order")
    private Long order;

}
