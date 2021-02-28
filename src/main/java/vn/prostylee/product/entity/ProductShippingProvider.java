package vn.prostylee.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import vn.prostylee.core.entity.AuditEntity;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_shipping_provider")
public class ProductShippingProvider extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "product_shipping_provider_seq", sequenceName = "product_shipping_provider_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_shipping_provider_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "shipping_provider_id")
    private Long shippingProviderId;

}