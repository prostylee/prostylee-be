package vn.prostylee.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Table(name = "product_payment_type")
public class ProductPaymentType extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "product_payment_type_seq", sequenceName = "product_payment_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_payment_type_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "payment_type_id")
    private Long paymentTypeId;

}
