package vn.prostylee.product.entity;

import lombok.Data;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Entity
@Data
@Table(name = "product_payment_type")
public class ProductPaymentType extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "product_payment_type_seq", sequenceName = "product_payment_type_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_payment_type_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "payment_type_id")
    private Long paymentTypeId;

}
