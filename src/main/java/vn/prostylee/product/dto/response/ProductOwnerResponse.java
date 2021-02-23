package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOwnerResponse {

    private Long id;

    private String name;

    private String logoUrl;

}
