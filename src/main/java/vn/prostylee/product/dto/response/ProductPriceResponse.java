package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceResponse implements Serializable {

    private Long id;

    private String name;

    private String sku;

    private Double price;

    private Double priceSale;

    private List<ProductAttributeResponse> productAttributes;
}
