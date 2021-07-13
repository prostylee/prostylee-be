package vn.prostylee.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOwnerResponse implements Serializable {

    private Long id;

    private String name;

    private String logoUrl;

    private String fullAddress;

}
